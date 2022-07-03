package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GenreFilmStorage {
    private final String GENRE_INSERT_SQL = "insert into GENRE_FILM(GENRE_ID, FILM_ID) values  (?, ?) ";
    private final String GENRE_DELETE_SQL = "delete from GENRE_FILM where  FILM_ID = ?";
    private final JdbcTemplate jdbcTemplate;

    public GenreFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void put(int genreId, int filmId) {
        jdbcTemplate.update(GENRE_INSERT_SQL, genreId, filmId);
    }

    public void deleteGenresByFilm(int filmId) {
        jdbcTemplate.update(GENRE_DELETE_SQL, filmId);
    }
}
