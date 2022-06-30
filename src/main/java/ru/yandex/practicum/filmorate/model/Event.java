package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Event {
    private int eventId;
    private long timestamp;
    private int userId;
    private EventType eventType;
    private Operation operation;
    private int entityId;

    public Event(int eventId, long timestamp, int userId, EventType eventType, Operation operation, int entityId) {
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
