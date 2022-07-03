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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final String FILM_INSERT_SQL = "insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPAA_ID) " +
            "values(?, ?, ?, ?, ?)";
    private final String FILM_UPDATE_SQL = "update FILM set NAME = ?, DESCRIPTION = ?,RELEASE_DATE = ?, DURATION = ?," +
            " MPAA_ID = ?   where FILM_ID = ?";
    private final String FILM_ALL_SQL = "select * from film  join mpaa on film.mpaa_id = mpaa.mpaa_id";
    private final String FILM_GET_SQL = "select * from film join mpaa on FILM.MPAA_id = mpaa.mpaa_id where FILM_ID = ?";
    private final String FILM_POPULAR_SQL = "select * from film LEFT JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID  " +
            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
    private final String FILM_GET_GENRE_SQL = "select * from film LEFT JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID " +
            "LEFT JOIN GENRE_FILM GF on FILM.FILM_ID = GF.FILM_ID " + "WHERE GF.GENRE_ID=?" +
            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
    private final String FILM_GET_YEAR_SQL = "select * from film LEFT JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID " +
            "WHERE EXTRACT(YEAR FROM CAST(film.RELEASE_DATE AS DATE)) =?" +
            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
    private final String FILM_GET_GENRE_AND_YEAR_SQL = "select * from film LEFT JOIN  LIKES  on" +
            " film.FILM_ID  = lIKES.FILM_ID " +
            "LEFT JOIN GENRE_FILM GF on FILM.FILM_ID = GF.FILM_ID " +
            "WHERE GF.GENRE_ID=? and EXTRACT(YEAR FROM CAST(film.RELEASE_DATE AS DATE)) =?" +
            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
    private final String FILM_SET_GENRE_SQL = "select G.GENRE_ID, G.NAME from GENRE AS G  join GENRE_FILM GF on" +
            " G.GENRE_ID = GF.GENRE_ID  where  GF.FILM_ID = ? ORDER BY G.GENRE_ID ";
    private final String FILM_SET_LIKES_SQL = "select USER_ID from LIKES  where  FILM_ID =?";
    private final String FILM_DELETE_SQL = "delete from FILM where FILM_ID = ? ";

    private final JdbcTemplate jdbcTemplate;
    private final GenreFilmStorage genreFilmStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreFilmStorage genreFilmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreFilmStorage = genreFilmStorage;
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(FILM_INSERT_SQL, new String[]{"film_id"});
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
            TreeSet<Genre> genresSet = new TreeSet<>(Comparator.comparing(Genre::getId));
            genresSet.addAll(film.getGenres());
            film.setGenres(genresSet);
            for (Genre genre : film.getGenres()) {
                genreFilmStorage.put(genre.getId(), film.getId());
            }
        }
        //if (film.getDirectors() != null) {
            addDirectorToFilm(filmId, film.getDirectors());
       // }
        return film;
    }

    @Override
    public Film put(Film film) {
        int test = jdbcTemplate.update(FILM_UPDATE_SQL
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
        if (film.getDirectors() == null || film.getDirectors().isEmpty()) {
            film.setDirectors(null);
        }
        if (test != 1) {
            throw new FilmNotFoundException("film not found");
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.queryForStream(
                        FILM_ALL_SQL
                        ,
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
                .queryForRowSet(FILM_GET_SQL, id);
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
            // добавить режиссера
            return film;
        }
        throw new FilmNotFoundException("Фильмы не найдены");
    }

    public List<Film> getBestFilms(int count, Integer genreId, Integer year) {
        if (genreId == null && year == null) {
            return jdbcTemplate.query(FILM_POPULAR_SQL, (rs, rowNum) -> makeFilm(rs), count);
        }
        if (genreId != null && year == null) {
            return jdbcTemplate.query(FILM_GET_GENRE_SQL, (f, rowNum) -> makeFilm(f), genreId, count);
        }
        if (genreId == null && year != null) {
            return jdbcTemplate.query(FILM_GET_YEAR_SQL, (f, rowNum) -> makeFilm(f), year, count);
        } else {
            return jdbcTemplate.query(FILM_GET_GENRE_AND_YEAR_SQL, (f, rowNum) -> makeFilm(f), genreId, year, count);
        }
    }

    @Override
    public Collection<Film> search(String query, List<String> by) {

        String sqlTitle =    "SELECT FILM_ID  FROM FILM  WHERE LOWER(NAME)  like  '%" + query.toLowerCase() + "%' ";
        String sqlDirector =  "SELECT  F.FILM_ID FROM FILM AS F JOIN DIRECTOR_FILM DF on F.FILM_ID = DF.FILM_ID JOIN DIRECTORS D on D.DIRECTOR_ID = DF.DIRECTOR_ID WHERE  LOWER(D.NAME) like '%" + query.toLowerCase() + "%' ";
        List<Film> films = new ArrayList<>();
        if(by.containsAll(List.of("director", "title"))) {
            films = jdbcTemplate.query(sqlDirector, (rs, rowNum) -> get(rs.getInt("film_id")));
            films.addAll(jdbcTemplate.query(sqlTitle, (rs, rowNum) -> get(rs.getInt("film_id"))));
        }
        else if(by.contains("title")){
             films = jdbcTemplate.query(sqlTitle, (rs, rowNum) -> get(rs.getInt("film_id")));
        }
        else if(by.contains("director")){
             films = jdbcTemplate.query(sqlDirector, (rs, rowNum) -> get(rs.getInt("film_id")));
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
        List<Genre> genres = jdbcTemplate.query(FILM_SET_GENRE_SQL, (gs, rowNum) -> makeGenre(gs), film.getId());
        if (genres.isEmpty()) {
            return film;
        }
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    private Film setLikes(Film film) {
        List<Integer> likes = jdbcTemplate.query(FILM_SET_LIKES_SQL, (gs, rowNum) -> gs.getInt("User_id"),
                film.getId());
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
        jdbcTemplate.update(FILM_DELETE_SQL,
                id);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        String sqlQuery = "SELECT film_id " +
                "FROM LIKES " +
                "WHERE user_id = ? " +
                "INTERSECT " +
                "SELECT film_id " +
                "FROM LIKES " +
                "WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        List<Film> commonFilms = new ArrayList<>();
        while (rowSet.next()) {
            commonFilms.add(get(rowSet.getInt("film_id")));
        }
        return commonFilms;
    }

    private Film setDirector(Film film) {
        log.info("FilmDbStorage => setDirector вошли в метод");
        String sql = "select d.director_id, d.name " +
                "from DIRECTORS AS d  " +
                "join director_film AS df on d.director_id = df.director_id  " +
                "where  df.film_id = " + film.getId() +
                " ORDER BY d.director_id ";
        List<Director> directors = jdbcTemplate.query(sql, (gs, rowNum) -> makeDirector(gs));
        log.info("FilmDbStorage => setDirector  List<Director> directors " + directors);
        if (directors.isEmpty()) {
            return film;
        }
        film.setDirectors(new TreeSet<>(directors));
        log.info("FilmDbStorage => setDirector  film " + film);
        return film;
    }

    private Director makeDirector(ResultSet gs) throws SQLException {
        log.info("FilmDbStorage => makeDirector  вошли в метод");
        return new Director(
                gs.getInt("director_id"),
                gs.getString("name")
        );
    }

    public void addDirectorToFilm(int filmId, TreeSet<Director> directors) {
        log.info("FilmDbStorage => addDirectorToFilm вошли в метод, filmId = {}, directors = {}",filmId,directors);
        String DIRECTOR_INSERT_TO_FILM = "INSERT INTO DIRECTOR_FILM (DIRECTOR_ID, FILM_ID) VALUES ( ?,? )";
        //String DIRECTOR_INSERT_TO_FILM = "insert into director_film (director_id, film_id) values ( ?, ? )";

       /* var var = directors.stream()
                .map(d -> jdbcTemplate.update(DIRECTOR_INSERT_TO_FILM, d.getId(),filmId));*/
        for (Director director : directors) {
            jdbcTemplate.update(DIRECTOR_INSERT_TO_FILM, director.getId(),filmId);
            log.info("FilmDbStorage => addDirectorToFilm director" + director);
        }
    }

    public void removeDirectorFromFilm(int filmId) {
        log.info("FilmDbStorage => removeDirectorFromFilm remove filmId " + filmId);
        String DIRECTOR_DELETE_FROM_FILM = "DELETE FROM director_film WHERE film_id=?";
        jdbcTemplate.update(DIRECTOR_DELETE_FROM_FILM, filmId);
    }





}
