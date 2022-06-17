package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {

        String sqlQuery = "insert into users (email, login, name, birthday) values(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, user.getBirthday());
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;

    }

    @Override
    public User put(User user) {
        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ?   where user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.queryForStream("""
                        select * from users order by user_id;
                        """,
                (rs, rowNum) ->
                        new User(
                                rs.getInt("user_id"),
                                rs.getString("email"),
                                rs.getString("login"),
                                rs.getString("name"),
                                rs.getDate("birthday")
                        )
        )
                .collect(Collectors.toList());
    }

    public Collection<User> getFriend(int id) {

        return null;
    }

    @Override
    public Optional<User> get(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where USER_ID = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday")
            );


            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> getFriends(int id) {

        String sql = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = "  + id + ");";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        return users;
    }

    @Override
    public void addFriends(int id, int friendId) {
        String sql1Query = "insert into FRIENDS(USER_ID, FRIEND_ID)  " +
                "values (?, ?)";
        jdbcTemplate.update(sql1Query,
                id,
                friendId);
    }

    @Override
    public void deleteFriends(int id, int friendId) {
        String sql1Query = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";

        jdbcTemplate.update(sql1Query,
                id,
                friendId);
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        String sql = "SELECT * From USERS where USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = "  + id + ") AND USER_ID IN (SELECT FRIEND_ID FROM FRIENDS where USER_ID = "  + otherId + ")";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        return users;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday")
        );

        return user;
    }
}