package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.GenreFilmStorage;
import ru.yandex.practicum.filmorate.storage.MPAADbStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorFilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final String FILM_INSERT_SQL = "insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPAA_ID, RATE) " +
            "values(?, ?, ?, ?, ?, ?)";
    private final String FILM_UPDATE_SQL = "update FILM set NAME = ?, DESCRIPTION = ?,RELEASE_DATE = ?, DURATION = ?," +
            " MPAA_ID = ?   where FILM_ID = ?";
 //   private final String FILM_UPDATE_RATE_SQL = "update FILM set RATE = ?  where FILM_ID = ?";
    private final String FILM_ALL_SQL = "select * from film  join mpaa on film.mpaa_id = mpaa.mpaa_id";
    private final String FILM_GET_SQL = "select * from film join mpaa on FILM.MPAA_id = mpaa.mpaa_id where FILM_ID = ?";
    private final String FILM_POPULAR_SQL = "select * from film LEFT JOIN  MARKS M2 on FILM.FILM_ID = M2.FILM_ID " +
            "GROUP BY FILM.FILM_ID,M2.USER_ID ORDER BY AVG(M2.MARK) DESC LIMIT ?";
    private final String FILM_GET_GENRE_SQL ="select * from film LEFT JOIN  MARKS M2 on FILM.FILM_ID = M2.FILM_ID " +
            "LEFT JOIN GENRE_FILM GF on FILM.FILM_ID = GF.FILM_ID  WHERE GF.GENRE_ID=? " +
            "GROUP BY FILM.FILM_ID,M2.USER_ID ORDER BY AVG(M2.MARK) DESC LIMIT ?";
    private final String FILM_GET_YEAR_SQL ="select * from film LEFT JOIN  MARKS M2 on FILM.FILM_ID = M2.FILM_ID " +
            "WHERE EXTRACT(YEAR FROM CAST(film.RELEASE_DATE AS DATE)) =? GROUP BY FILM.FILM_ID,M2.USER_ID " +
            "ORDER BY AVG(M2.MARK) DESC LIMIT ?";
    private final String FILM_GET_GENRE_AND_YEAR_SQL ="select * from film " +
            "LEFT JOIN  MARKS M2 on FILM.FILM_ID = M2.FILM_ID LEFT JOIN GENRE_FILM GF on FILM.FILM_ID = GF.FILM_ID" +
            "  WHERE GF.GENRE_ID=? and EXTRACT(YEAR FROM CAST(film.RELEASE_DATE AS DATE))=? GROUP BY M2.USER_ID" +
            " ORDER BY AVG(M2.MARK) DESC LIMIT ?";

    //        private final String FILM_POPULAR_SQL = "select * from film LEFT JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID  " +
    //           "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
//    private final String FILM_GET_GENRE_SQL = "select * from film LEFT JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID " +
//            "LEFT JOIN GENRE_FILM GF on FILM.FILM_ID = GF.FILM_ID " + "WHERE GF.GENRE_ID=?" +
//            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
//    private final String FILM_GET_YEAR_SQL = "select * from film LEFT JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID " +
//            "WHERE EXTRACT(YEAR FROM CAST(film.RELEASE_DATE AS DATE)) =?" +
//            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
//    private final String FILM_GET_GENRE_AND_YEAR_SQL = "select * from film LEFT JOIN  LIKES  on" +
//            " film.FILM_ID  = lIKES.FILM_ID " +
//            "LEFT JOIN GENRE_FILM GF on FILM.FILM_ID = GF.FILM_ID " +
//            "WHERE GF.GENRE_ID=? and EXTRACT(YEAR FROM CAST(film.RELEASE_DATE AS DATE)) =?" +
//            "GROUP BY FILM.FILM_ID,PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
    private final String FILM_SET_GENRE_SQL = "select G.GENRE_ID, G.NAME from GENRE AS G  join GENRE_FILM GF on" +
            " G.GENRE_ID = GF.GENRE_ID  where  GF.FILM_ID = ? ORDER BY G.GENRE_ID ";
    private final String FILM_SET_MARKS_SQL = "select * from MARKS  where  FILM_ID =?";
    private final String FILM_DELETE_SQL = "delete from FILM where FILM_ID = ? ";
    private final String SET_DIRECTOR = "select d.director_id, d.name " +
            "from DIRECTORS AS d  " +
            "join director_film AS df on d.director_id = df.director_id  " +
            "where  df.film_id = ? ORDER BY d.director_id ";
    private final String FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_MARKS =
            "SELECT FILM.FILM_ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.MPAA_ID, FILM.RATE " +
                    "FROM FILM " +
                    "LEFT JOIN MARKS M on FILM.FILM_ID = M.FILM_ID " +
                    "LEFT JOIN DIRECTOR_FILM DF on FILM.FILM_ID = DF.FILM_ID " +
                    "WHERE DF.DIRECTOR_ID=?  AND RATE > 5 GROUP BY FILM.FILM_ID " +
                    "ORDER BY FILM.RATE DESC;";
    private final String FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_YEARS =
            "SELECT FILM.FILM_ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.MPAA_ID, FILM.RATE " +
                    "FROM FILM " +
                    "LEFT JOIN DIRECTOR_FILM DF on FILM.FILM_ID = DF.FILM_ID " +
                    "LEFT JOIN MARKS M on FILM.FILM_ID = M.FILM_ID " +
                    "WHERE DF.DIRECTOR_ID =? AND RATE > 5 GROUP BY FILM.FILM_ID " +
                    "ORDER BY FILM.RELEASE_DATE;";
    private final String GET_COMMON_FILMS = "SELECT F.film_id FROM FILM AS F JOIN MARKS M on F.FILM_ID = M.FILM_ID WHERE user_id = ? AND F.RATE > 5 INTERSECT SELECT film_id FROM MARKS WHERE user_id = ? ";
    private final String SEARCH_BY_TITLE = "SELECT FILM_ID  FROM FILM  WHERE LOWER(NAME)  like ? ";
    private final String SEARCH_BY_DIRECTOR = "SELECT  F.FILM_ID FROM FILM AS F JOIN DIRECTOR_FILM DF on F.FILM_ID = DF.FILM_ID " +
            "JOIN DIRECTORS D on D.DIRECTOR_ID = DF.DIRECTOR_ID WHERE  LOWER(D.NAME) like ? ;";


    private final JdbcTemplate jdbcTemplate;
    private final GenreFilmStorage genreFilmStorage;
    private final MPAADbStorage mpaaDbStorage;
    private final DirectorFilmStorage directorFilmStorage;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenreFilmStorage genreFilmStorage,
                         MPAADbStorage mpaaDbStorage,
                         DirectorFilmStorage directorFilmStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreFilmStorage = genreFilmStorage;
        this.mpaaDbStorage = mpaaDbStorage;
        this.directorFilmStorage = directorFilmStorage;
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
            stmt.setInt(6, 0); //тк в тз нет конкретных указаний, рейтинг вновь созданного фильма равен нулю
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
        directorFilmStorage.addDirectorToFilm(filmId, film.getDirectors());
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
            directorFilmStorage.removeDirectorFromFilm(film.getId());
            directorFilmStorage.addDirectorToFilm(film.getId(), film.getDirectors());
        }
        if (film.getDirectors() == null || film.getDirectors().isEmpty()) {
            film.setDirectors(null);
        }
        if (test != 1) {
            throw new FilmNotFoundException("film not found");
        }
        setMarks(film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.queryForStream(
                FILM_ALL_SQL
                ,
                (rs, rowNum) -> makeFilm(rs))
                .collect(Collectors.toList());
    }

    @Override
    public Film get(int id) {
        //   updateRateFilm();
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
                            filmRows.getString(9)),
                    filmRows.getDouble("rate")
            );
            setGenre(film);
            setMarks(film);
            setDirector(film);
            return film;
        }
        throw new FilmNotFoundException("Фильмы не найдены");
    }

    @Override
    public List<Film> getBestFilms(int count, Integer genreId, Integer year) {
        if (genreId != null && year != null) {
            return jdbcTemplate.query(FILM_GET_GENRE_AND_YEAR_SQL, (f, rowNum) -> makeFilm(f),genreId,year, count);

       }
        if (genreId != null && year == null) {
            return jdbcTemplate.query(FILM_GET_GENRE_SQL, (f, rowNum) -> makeFilm(f), genreId, count);
       }
       if (genreId == null && year != null) {
            return jdbcTemplate.query(FILM_GET_YEAR_SQL, (f, rowNum) -> makeFilm(f), year, count);
        } else {
           return jdbcTemplate.query(FILM_POPULAR_SQL, (rs, rowNum) -> makeFilm(rs), count);
       }
    }

    @Override
    public Collection<Film> search(String query, List<String> by) {
        String search = "%" + query.toLowerCase() + "%";
        List<Film> films = new ArrayList<>();
        if (by.containsAll(List.of("director", "title"))) {
            films = jdbcTemplate.query(SEARCH_BY_DIRECTOR, (rs, rowNum) ->
                    get(rs.getInt("film_id")), search);
            films.addAll(jdbcTemplate.query(SEARCH_BY_TITLE, (rs, rowNum) ->
                    get(rs.getInt("film_id")), search));
        } else if (by.contains("title")) {
            films = jdbcTemplate.query(SEARCH_BY_TITLE, (rs, rowNum) ->
                    get(rs.getInt("film_id")), search);
        } else if (by.contains("director")) {
            films = jdbcTemplate.query(SEARCH_BY_DIRECTOR, (rs, rowNum) ->
                    get(rs.getInt("film_id")), search);
        }
        return films;
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(
                rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_Date").toLocalDate(),
                rs.getInt("duration"),
                new MPAA(rs.getInt("mpaa_id"),
                        rs.getString(9)),
                rs.getDouble("rate"));
        setGenre(film);
        setMarks(film);
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

    private Film setMarks(Film film) {
        List<Mark> marks = jdbcTemplate.query(FILM_SET_MARKS_SQL, (gs, rowNum) -> makeMark(gs),
                film.getId());
        if (marks.isEmpty()) {
            film.setMarks(new HashSet<>());
        }
        film.setMarks(new HashSet<>(marks));
        return film;
    }

    private Mark makeMark(ResultSet rs) throws SQLException {
        return new Mark(
                rs.getInt("user_id"),
                rs.getInt("film_id"),
                rs.getInt("mark")
        );
    }

    public void updateRateFilm(int filmId) {
        String UPDATE_RATE = "UPDATE FILM AS F SET RATE = ((SELECT AVG(MARK) FROM MARKS WHERE MARKS.FILM_ID = F.FILM_ID)) WHERE F.FILM_ID = ? ";
        jdbcTemplate.update(UPDATE_RATE, filmId);
    }

    private Genre makeGenre(ResultSet gs) throws SQLException {
        return new Genre(
                gs.getInt("genre_id"),
                gs.getString("name")
        );
    }

    @Override
    public void deleteFilm(int id) {
        jdbcTemplate.update(FILM_DELETE_SQL,
                id);
    }

    private Film setDirector(Film film) {
        List<Director> directors = jdbcTemplate.query(SET_DIRECTOR, (gs, rowNum) -> makeDirector(gs), film.getId());
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

    @Override
    public List<Film> getAllFilmsOfDirectorSortedByMarks(int id) {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(FILMS_SELECT_ALL_OF_DIRECTOR_SORTED_BY_MARKS, id);
        while (filmsRows.next()) {
            Film film = new Film(
                    filmsRows.getInt("film_id"),
                    filmsRows.getString("name"),
                    filmsRows.getString("description"),
                    filmsRows.getDate("release_Date").toLocalDate(),
                    filmsRows.getInt("duration"),
                    new MPAA(filmsRows.getInt("mpaa_id"),
                            mpaaDbStorage.getById(filmsRows.getInt("mpaa_id")).getName()
                    ),
                    filmsRows.getDouble("rate"));
            film.setGenres(setGenre(film).getGenres());
            film.setDirectors(setDirector(film).getDirectors());
            setMarks(film);
            films.add(film);
        }
        return films;
    }

    @Override
    public List<Film> getAllFilmsOfDirectorSortedByYears(int id) {
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
                    ),
                    filmsRows.getDouble("rate"));
            film.setGenres(setGenre(film).getGenres());
            film.setDirectors(setDirector(film).getDirectors());
            setMarks(film);

            films.add(film);
        }
        return films;
    }


    public List<Film> getCommonFilms(int userId, int friendId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(GET_COMMON_FILMS, userId, friendId);
        List<Film> commonFilms = new ArrayList<>();

        while (rowSet.next()) {
            commonFilms.add(get(rowSet.getInt("film_id")));
        }
        return commonFilms;
    }
}
