package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GenreFilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   public  void put(int genreId, int filmId){
        String sql1Query = "insert into GENRE_FILM(GENRE_ID, FILM_ID)   " +
                "values  (?, ?) ";
        jdbcTemplate.update(sql1Query, genreId, filmId);
    }

    public void deleteGenresByFilm(int filmId){
        String sql2Query = "delete from GENRE_FILM where  FILM_ID = ?";
        jdbcTemplate.update(sql2Query,filmId);
    }
}
