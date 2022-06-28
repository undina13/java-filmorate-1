package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private int reviewId;
    private String content;
    private boolean isPositive;
    private int userId;
    private int filmId;
    private int useful;

    public Review(int reviewId, String content, boolean isPositive, int userId, int filmId, int useful) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.useful = useful;
    }
}
