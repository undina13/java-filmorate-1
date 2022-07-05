package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
public class MarksStorage {

    private final String MARK_INSERT_SQL = "insert into MARKS(USER_ID, FILM_ID, MARK)  values (?, ?, ?)";
    private final String MARK_DELETE_SQL = "delete from MARKS where USER_ID = ? and  FILM_ID = ?";
    String DELETE_ALL_MARKS = "delete from MARKS where FILM_ID = ? ";

    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;
    private  final FilmDbStorage filmDbStorage;

    @Autowired
    public MarksStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
        this.filmDbStorage = filmDbStorage;
    }

    public void putMark(int filmId, int userId, int mark) {
        jdbcTemplate.update(MARK_INSERT_SQL,
                userId,
                filmId,
                mark);



        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                userId,
                EventType.MARK,
                Operation.ADD,
                filmId));
    }

    public void deleteMark(int filmId, int userId) {
        jdbcTemplate.update(MARK_DELETE_SQL,
                userId,
                filmId);



        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                userId,
                EventType.MARK,
                Operation.REMOVE,
                filmId));
    }

    public void deleteAllMarks(int filmId) {
        jdbcTemplate.update(DELETE_ALL_MARKS,
                filmId);

    }
}
