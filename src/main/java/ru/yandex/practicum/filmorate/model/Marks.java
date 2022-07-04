package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Marks {
    int user_id;
    int film_id;
    int mark;

    public Marks(int user_id, int film_id) {
        this.user_id = user_id;
        this.film_id = film_id;
    }
}
