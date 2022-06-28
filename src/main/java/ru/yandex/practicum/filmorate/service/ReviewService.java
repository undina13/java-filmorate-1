package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class ReviewService {
    private ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }


    public Optional<Review> getReviewById(Integer reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public Review create(Review review) {
        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        return reviewStorage.update(review);
    }

    public void removeReview(Integer reviewId) {
        reviewStorage.removeReview(reviewId);
    }

    public void putLikeReviewById(Integer reviewId, Integer userId) {
        reviewStorage.putLikeReviewById(reviewId, userId);
    }

    public void putDislikeReviewById(Integer reviewId, Integer userId) {
        reviewStorage.putDislikeReviewById(reviewId, userId);
    }

    public void removeLike(Integer reviewId, Integer userId) {
        reviewStorage.putDislikeReviewById(reviewId, userId);
    }

    public void removeDislike(Integer reviewId, Integer userId) {
        reviewStorage.putLikeReviewById(reviewId, userId);
    }

    public Collection<Review> getReviewsFilmById(Integer count) {
        return reviewStorage.getReviewsFilmById(count);
    }

    public Collection<Review> findAll() {
        return reviewStorage.findAll();
    }
}
