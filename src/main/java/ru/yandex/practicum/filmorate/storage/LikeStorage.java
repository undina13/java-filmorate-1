package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
public class LikeStorage {
    private final String LIKE_INSERT_SQL = "insert into LIKES(USER_ID, FILM_ID)  values (?, ?)";
    private final String LIKE_DELETE_SQL = "delete from LIKES where USER_ID = ? and  FILM_ID = ?";
    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    @Autowired
    public LikeStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    public void putLike(int filmId, int userId) {
        jdbcTemplate.update(LIKE_INSERT_SQL,
                userId,
                filmId);

        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                userId,
                EventType.LIKE,
                Operation.ADD,
                filmId));
    }

    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update(LIKE_DELETE_SQL,
                userId,
                filmId);

        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                userId,
                EventType.LIKE,
                Operation.REMOVE,
                filmId));
    }

    public void deleteAllLike(int filmId) {
        String sql1Query = "delete from LIKES where FILM_ID = ? ";
        jdbcTemplate.update(sql1Query,

                filmId);
    }
}
