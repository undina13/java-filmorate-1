package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;
import java.util.Optional;

@RestController
public class ReviewController {
    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews/{id}")
    public Optional<Review> findGenre(@PathVariable("id") Integer reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @PostMapping(value = "/reviews")
    public Review create(@RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping(value = "/reviews")
    public Review update(@RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/reviews/{id}")
    public void removeReview(@PathVariable("id") Integer reviewId) {
        reviewService.removeReview(reviewId);
    }

    @PutMapping(value = "/reviews/{id}/like/{userId}")
    public void putLikeReviewById(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        reviewService.putLikeReviewById(reviewId, userId);
    }

    @PutMapping(value = "/reviews/{id}/dislike/{userId}")
    public void putDislikeReviewById(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        reviewService.putDislikeReviewById(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        reviewService.removeLike(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable("id") Integer reviewId, @PathVariable("userId") Integer userId) {
        reviewService.removeDislike(reviewId, userId);
    }

    @GetMapping("/reviews")
    public Collection<Review> getReviewsFilmById(@RequestParam(required = false) Integer filmId,
                                                 @RequestParam(required = false) Integer count) {
        return reviewService.getReviewsFilmById(filmId, count);
    }
}
