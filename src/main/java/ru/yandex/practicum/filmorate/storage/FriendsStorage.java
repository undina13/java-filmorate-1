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
public class FriendsStorage {
    private final String FRIEND_INSERT_SQL = "insert into FRIENDS(USER_ID, FRIEND_ID) values (?, ?)";
    private final String FRIEND_DELETE_SQL = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    @Autowired
    public FriendsStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    public void addFriends(int id, int friendId) {
        jdbcTemplate.update(FRIEND_INSERT_SQL,
                id,
                friendId);

        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                id,
                EventType.FRIEND,
                Operation.ADD,
                friendId));
    }

    public void deleteFriends(int id, int friendId) {

        jdbcTemplate.update(FRIEND_DELETE_SQL,
                id,
                friendId);
        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                id,
                EventType.FRIEND,
                Operation.REMOVE,
                friendId));
    }
}
