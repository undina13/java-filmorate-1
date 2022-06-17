package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPAA;
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


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
       if (film.getGenres()!= null){
           for( Genre genre :film.getGenres()){
               String sql1Query = "insert into GENRE_FILM(GENRE_ID, FILM_ID)  " +
                       "values (?, ?)";
               jdbcTemplate.update(sql1Query,
                       genre.getId(),
                       filmId);
           }
       }
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
        if (film.getGenres()!= null){
            String sql2Query = "delete from GENRE_FILM where  FILM_ID = ?";
            jdbcTemplate.update(sql2Query,
                    film.getId());
            TreeSet<Genre> genresSet =  new TreeSet<>(Comparator.comparing(Genre::getId));
               genresSet.addAll(film.getGenres());
               film.setGenres(genresSet);
            for( Genre genre :genresSet){
                String sql1Query = "insert into GENRE_FILM(GENRE_ID, FILM_ID)   " +
                        "values  (?, ?) ";
              jdbcTemplate.update(sql1Query,
                        genre.getId(),
                        film.getId());
            }
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
            return film;
        }
        throw new FilmNotFoundException("Фильмы не найдены");
    }

    public List<Film> getBestFilms(int count) {
        String sql = "select * from film JOIN  LIKES  on film.FILM_ID  = lIKES.FILM_ID  GROUP BY LIKES.FILM_ID, PUBLIC.LIKES.USER_ID ORDER BY COUNT(LIKES.USER_ID) DESC" ;
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        if (films.isEmpty()) {
            films = new ArrayList<>(getAll());
        }
        if(films.size() > count){
            films = films.subList(0, count);
        }
        return films;
    }

    @Override
    public void putLike(int filmId, int userId) {
        String sql1Query = "insert into LIKES(USER_ID, FILM_ID)  " +
                "values (?, ?)";
        jdbcTemplate.update(sql1Query,
                userId,
                filmId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql1Query = "delete from LIKES where USER_ID = ? and  FILM_ID = ? " ;
        jdbcTemplate.update(sql1Query,
                userId,
                filmId);
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
        return film;
    }

    private Film  setGenre(Film film){
            String sql = "select G.GENRE_ID, G.NAME from GENRE AS G  join GENRE_FILM GF on G.GENRE_ID = GF.GENRE_ID  where  GF.FILM_ID = " + film
                    .getId() + " ORDER BY G.GENRE_ID ";
            List<Genre> genres = jdbcTemplate.query(sql, (gs, rowNum) -> makeGenre(gs));
            if (genres.isEmpty()){
                return film;
            }
            film.setGenres(new HashSet<>(genres));
        return film;
    }

    private Film setLikes(Film film){
        String sql = "select USER_ID from LIKES  where  FILM_ID = " + film.getId();
        List<Integer> likes = jdbcTemplate.query(sql, (gs, rowNum) -> gs.getInt("User_id"));
        if (likes.isEmpty()){
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

    @Override
    public void deleteAll() {

    }

    public void deleteFilm(int id){
        String sql1Query = "delete from FILM where FILM_ID = ? " ;
        jdbcTemplate.update(sql1Query,
                id);
    }

}
