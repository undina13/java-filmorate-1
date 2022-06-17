package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPAA {
    int id;
    String name;

    public MPAA(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
