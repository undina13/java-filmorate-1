package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.TreeSet;

@Component
public class DirectorFilmDbStorage implements DirectorFilmStorage {
    private final String DIRECTOR_INSERT_TO_FILM = "INSERT INTO DIRECTOR_FILM (DIRECTOR_ID, FILM_ID) VALUES ( ?,? )";
    private final String DIRECTOR_DELETE_FROM_FILM = "DELETE FROM director_film WHERE film_id=?";

    private final JdbcTemplate jdbcTemplate;

    public DirectorFilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addDirectorToFilm(int filmId, TreeSet<Director> directors) {
        for (Director director : directors) {
            jdbcTemplate.update(DIRECTOR_INSERT_TO_FILM, director.getId(), filmId);
        }
    }

    @Override
    public void removeDirectorFromFilm(int filmId) {
        jdbcTemplate.update(DIRECTOR_DELETE_FROM_FILM, filmId);
    }
}
