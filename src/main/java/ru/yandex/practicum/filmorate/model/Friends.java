package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friends {
    int user1_id;
    int user2_id;
    boolean friendship;

    public Friends(int user1_id, int user2_id, boolean friendship) {
        this.user1_id = user1_id;
        this.user2_id = user2_id;
        this.friendship = friendship;
    }
}
