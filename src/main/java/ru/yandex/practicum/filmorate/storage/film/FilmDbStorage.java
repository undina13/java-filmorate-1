package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.storage.GenreFilmStorage;
import ru.yandex.practicum.filmorate.storage.MPAADbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreFilmStorage genreFilmStorage;
    private final MPAADbStorage mpaaDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreFilmStorage genreFilmStorage, MPAADbStorage mpaaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreFilmStorage = genreFilmStorage;
        this.mpaaDbStorage = mpaaDbStorage;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPAA_ID) values(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int filmId = keyHolder.getKey().intValue();
        film.setId(filmId);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreFilmStorage.put(genre.getId(), film.getId());
            }
        }
        addDirectorToFilm(filmId, film.getDirectors());
        return film;
    }

    @Override
    public Film put(Film film) {
        String sqlQuery = "update FILM set NAME = ?, DESCRIPTION = ?,RELEASE_DATE = ?, DURATION = ?, MPAA_ID = ?   where FILM_ID = ?";
        int test = jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , Date.valueOf(film.getReleaseDate())
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        if (film.getGenres() != null) {
            genreFilmStorage.deleteGenresByFilm(film.getId());
            TreeSet<Genre> genresSet = new TreeSet<>(Comparator.comparing(Genre::getId));
            genresSet.addAll(film.getGenres());
            film.setGenres(genresSet);
            for (Genre genre : genresSet) {
                genreFilmStorage.put(genre.getId(), film.getId());
            }
        }
        if (film.getDirectors() != null) {
            removeDirectorFromFilm(film.getId());
            addDirectorToFilm(film.getId(), film.getDirectors());
        }
        if (film.getDirectors() == null || film.getDirectors().isEmpty()) {   // заглушка для тестов постман
            film.setDirectors(null);
        }
        if (test != 1) {
            throw new FilmNotFoundException("film not found");
        }

        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.queryForStream("""
                                select * from film  join mpaa on film.mpaa_id = mpaa.mpaa_id ;
                                """,
                        (rs, rowNum) ->
                                new Film(
                                        rs.getInt("film_id"),
                                        rs.getString("name"),
                                        rs.getString("description"),
                                        rs.getDate("release_Date").toLocalDate(),
                                        rs.getInt("duration"),
                                        new MPAA(rs.getInt("mpaa_id"),
                                                rs.getString(8))
                                )
                )
                .map(this::setGenre)
                .map(this::setLikes)
                .map(this::setDirector)
                .collect(Collectors.toList());
    }

    @Override
    public Film get(int id) {
        SqlRowSet filmRows = jdbcTemplate
                .queryForRowSet("select * from film join mpaa on FILM.MPAA_id = mpaa.mpaa_id where FILM_ID = ?", id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_Date").toLocalDate(),
                    filmRows.getInt("duration"),
                    new MPAA(filmRows.getInt("mpaa_id"),
                            filmRows.getString(8))
            );
            setGenre(film);
            setLikes(film);
            setDirector(film);
            return film;
        }
        throw new FilmNotFoundException("Фильмы не найдены");
    }

    public List<Film> getBestFilms(int count) {
        String sql = "select * from film JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID  GROUP BY LIKES.FILM_ID, PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (films.isEmpty()) {
            films = new ArrayList<>(getAll());
        }
        if (films.size() > count) {
            films = films.subList(0, count);
        }
        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                new MPAA(rs.getInt("mpaa_id"),
                        rs.getString(8)));
        setGenre(film);
        setLikes(film);
        setDirector(film);
        return film;
    }

    private Film setGenre(Film film) {
        String sql = "select G.GENRE_ID, G.NAME " +
                "from GENRE AS G  " +
                "join GENRE_FILM GF on G.GENRE_ID = GF.GENRE_ID  " +
                "where  GF.FILM_ID = " + film.getId() +
                " ORDER BY G.GENRE_ID ";
        List<Genre> genres = jdbcTemplate.query(sql, (gs, rowNum) -> makeGenre(gs));
        if (genres.isEmpty()) {
            return film;
        }
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    private Film setLikes(Film film) {
        String sql = "select USER_ID from LIKES  where  FILM_ID = " + film.getId();
        List<Integer> likes = jdbcTemplate.query(sql, (gs, rowNum) -> gs.getInt("User_id"));
        if (likes.isEmpty()) {
            return film;
        }
        film.setLikes(new HashSet<>(likes));
        return film;
    }

    private Genre makeGenre(ResultSet gs) throws SQLException {
        return new Genre(
                gs.getInt("genre_id"),
                gs.getString("name")
        );
    }

    public void deleteFilm(int id) {
        String sql1Query = "delete from FILM where FILM_ID = ? ";
        jdbcTemplate.update(sql1Query,
                id);
    }

    private Film setDirector(Film film) {
        String sql = "select d.director_id, d.name " +
                "from DIRECTORS AS d  " +
                "join director_film AS df on d.director_id = df.director_id  " +
                "where  df.film_id = " + film.getId() +
                " ORDER BY d.director_id ";
        List<Director> directors = jdbcTemplate.query(sql, (gs, rowNum) -> makeDirector(gs));
        if (directors.isEmpty()) {
            return film;
        }
        film.setDirectors(new TreeSet<>(directors));
        return film;
    }

    private Director makeDirector(ResultSet gs) throws SQLException {
        return new Director(
                gs.getInt("director_id"),
                gs.getString("name")
        );
    }

    public void addDirectorToFilm(int filmId, TreeSet<Director> directors) {
        String DIRECTOR_INSERT_TO_FILM = "INSERT INTO DIRECTOR_FILM (DIRECTOR_ID, FILM_ID) VALUES ( ?,? )";
        for (Director director : directors) {
            jdbcTemplate.update(DIRECTOR_INSERT_TO_FILM, director.getId(), filmId);
        }
    }

    public void removeDirectorFromFilm(int filmId) {
        String DIRECTOR_DELETE_FROM_FILM = "DELETE FROM director_film WHERE film_id=?";
        jdbcTemplate.update(DIRECTOR_DELETE_FROM_FILM, filmId);
    }

    @Override
    public List<Film> getAllFilmsOfDirectorSortedByLikes(int id) {
        log.info("FilmDbStorage => getAllFilmsOfDirectorSortedByLikes вошли в метод");
        String FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_LIKES = """
                        SELECT FILM.FILM_ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.MPAA_ID
                         FROM FILM
                         LEFT JOIN LIKES L on FILM.FILM_ID = L.FILM_ID
                         LEFT JOIN DIRECTOR_FILM DF on FILM.FILM_ID = DF.FILM_ID
                         WHERE DF.DIRECTOR_ID=?
                         GROUP BY FILM.FILM_ID
                         ORDER BY COUNT(L.FILM_ID) DESC
                """;

        List<Film> films = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_LIKES, id);
        while (filmsRows.next()) {
            Film film = new Film(
                    filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("release_Date").toLocalDate(),
                    filmsRows.getInt("duration"),
                    new MPAA(filmsRows.getInt("mpaa_id"),
                            mpaaDbStorage.getById(filmsRows.getInt("mpaa_id")).getName()
                    ));
            film.setGenres(setGenre(film).getGenres());
            film.setDirectors(setDirector(film).getDirectors());
            films.add(film);
        }
        return films;
    }

    @Override
    public List<Film> getAllFilmsOfDirectorSortedByYears(int id) {
        String FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_YEARS = """
                SELECT FILM.FILM_ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.MPAA_ID
                FROM FILM
                LEFT JOIN DIRECTOR_FILM DF on FILM.FILM_ID = DF.FILM_ID
                WHERE DF.DIRECTOR_ID =?
                ORDER BY FILM.RELEASE_DATE
                 """;

        List<Film> films = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_YEARS, id);
        while (filmsRows.next()) {
            Film film = new Film(
                    filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("release_Date").toLocalDate(),
                    filmsRows.getInt("duration"),
                    new MPAA(filmsRows.getInt("mpaa_id"),
                            mpaaDbStorage.getById(filmsRows.getInt("mpaa_id")).getName()
                    ));
            film.setGenres(setGenre(film).getGenres());
            film.setDirectors(setDirector(film).getDirectors());
            films.add(film);
        }
        return films;
    }


}
