package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void putLike(int filmId, int userId) {
        String sql1Query = "insert into LIKES(USER_ID, FILM_ID)  " +
                "values (?, ?)";
        jdbcTemplate.update(sql1Query,
                userId,
                filmId);
    }


    public void deleteLike(int filmId, int userId) {
        String sql1Query = "delete from LIKES where USER_ID = ? and  FILM_ID = ? ";
        jdbcTemplate.update(sql1Query,
                userId,
                filmId);
    }
}
