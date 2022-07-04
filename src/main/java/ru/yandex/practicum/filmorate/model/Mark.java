package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mark {
    int user_id;
    int film_id;
    int mark;

    public Mark(int user_id, int film_id, int mark) {
        this.user_id = user_id;
        this.film_id = film_id;
        this.mark = mark;
    }
}
