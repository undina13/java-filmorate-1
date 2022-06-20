package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriends(int id, int friendId) {
        String sql1Query = "insert into FRIENDS(USER_ID, FRIEND_ID)  " +
                "values (?, ?)";
        jdbcTemplate.update(sql1Query,
                id,
                friendId);
    }

    public void deleteFriends(int id, int friendId) {
        String sql1Query = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql1Query,
                id,
                friendId);
    }
}
