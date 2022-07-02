package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final String USER_INSERT_SQL = "insert into users (email, login, name, birthday) values(?, ?, ?, ?)";
    private final String USER_UPDATE_SQL = "update users set email = ?, login = ?, name = ?, birthday = ?   " +
            "where user_id = ?";
    private final String USER_ALL_SQL = "SELECT * From USERS ";
    private final String USER_GET_SQL = "select * from users where USER_ID = ?";
    private final String USER_GET_FRIENDS_SQL = "SELECT * From USERS where USER_ID IN " +
            "(SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)";
    private final String USER_COMMON_SQL = "SELECT * From USERS where USER_ID IN " +
            "(SELECT FRIEND_ID FROM FRIENDS where USER_ID =?) AND USER_ID IN" +
            " (SELECT FRIEND_ID FROM FRIENDS where USER_ID = ?)";
    private final String USER_SET_FRIENDS_SQL = "SELECT USER_ID From USERS where USER_ID IN " +
            "(SELECT FRIEND_ID FROM FRIENDS where USER_ID =?)";
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(USER_INSERT_SQL, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User put(User user) {
        jdbcTemplate.update(USER_UPDATE_SQL
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(USER_ALL_SQL, (rs, rowNum) -> makeUser(rs));
        return users;
    }

    @Override
    public Optional<User> get(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(USER_GET_SQL, id);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            setFriends(user);
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public List<User> getFriends(int id) {
        List<User> users = jdbcTemplate.query(USER_GET_FRIENDS_SQL, (rs, rowNum) -> makeUser(rs), id);
        return users;
    }



    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        List<User> users = jdbcTemplate.query(USER_COMMON_SQL, (rs, rowNum) -> makeUser(rs), id, otherId);
        return users;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        );
        setFriends(user);
        return user;
    }

    private User setFriends(User user) {
        List<Integer> users = jdbcTemplate.query(USER_SET_FRIENDS_SQL, (rs, rowNum) -> rs.getInt("user_id"),
                user.getId());
        if (!users.isEmpty()) {
            user.setFriends(new HashSet<>(users));
        }
        return user;
    }
}
