package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class EventStorage {
    private final JdbcTemplate jdbcTemplate;

    public EventStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createEvent(Event event) {
        String sqlQuery = "insert into events (timestamp, user_id, event_type, operation, entity_id) values(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"event_id"});
            stmt.setTimestamp(1, event.getTimestamp());
            stmt.setInt(2, event.getUserId());
            stmt.setString(3, event.getEventType().toString());
            stmt.setString(4, event.getOperation().toString());
            stmt.setInt(5, event.getEntityId());
            return stmt;
        });

    }

    public List<Event> getEventByUserId(int userId) {
        String sql = "SELECT * From EVENTS WHERE USER_ID = " + userId;
        List<Event> events = jdbcTemplate.query(sql, (rs, rowNum) -> makeEvent(rs));
        return events;
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        Event event = new Event(
                rs.getInt("event_id"),
                rs.getTimestamp("timestamp"),
                rs.getInt("user_id"),
                EventType.valueOf(rs.getString("event_type")),
                Operation.valueOf(rs.getString("operation")),
                rs.getInt("entity_id")
        );
        return event;
    }
}
