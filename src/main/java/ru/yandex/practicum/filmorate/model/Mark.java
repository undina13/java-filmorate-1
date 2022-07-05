package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class Mark {
    int user_id;
    int film_id;

    @Min(1)
    @Max(10)
    int mark;

    public Mark(int user_id, int film_id, int mark) {
        this.user_id = user_id;
        this.film_id = film_id;
        this.mark = mark;
    }
}
