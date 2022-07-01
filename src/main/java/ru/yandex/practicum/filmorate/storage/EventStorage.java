package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
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
    private final String EVENT_INSERT_SQL = "insert into events" +
            " (timestamp, user_id, event_type, operation, entity_id) values(?, ?, ?, ?, ?)";
    private final String EVENT_GET_SQL = "SELECT * From EVENTS WHERE USER_ID =? ";
    private final JdbcTemplate jdbcTemplate;

    public EventStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createEvent(Event event) {
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(EVENT_INSERT_SQL, new String[]{"event_id"});
            stmt.setLong(1, event.getTimestamp());
            stmt.setInt(2, event.getUserId());
            stmt.setString(3, event.getEventType().toString());
            stmt.setString(4, event.getOperation().toString());
            stmt.setInt(5, event.getEntityId());
            return stmt;
        });
    }

    public List<Event> getEventByUserId(int userId) {
        List<Event> events = jdbcTemplate.query(EVENT_GET_SQL, (rs, rowNum) -> makeEvent(rs), userId);
        return events;
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        Event event = new Event(
                rs.getInt("event_id"),
                rs.getLong("timestamp"),
                rs.getInt("user_id"),
                EventType.valueOf(rs.getString("event_type")),
                Operation.valueOf(rs.getString("operation")),
                rs.getInt("entity_id")
        );
        return event;
    }
}
