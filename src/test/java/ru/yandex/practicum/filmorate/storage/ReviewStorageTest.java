package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ReviewStorageTest {
    private final ReviewStorage reviewStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private Review review;
    private Film film;
    private User user;

    @BeforeEach
    void setUp() {
        film = new Film(1, "tort", "test", LocalDate.of(2010, 2, 2),
                100, new MPAA(5, "NC-17"));
        filmDbStorage.create(film);
        user = new User(1, "tortis", "dmitry", "tortiss00@yandex.ru",
                LocalDate.of(1992, 4, 23));
        userDbStorage.create(user);
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
    }

    @Test
    void getReviewById() {
        Optional<Review> reviewOptional = reviewStorage.getReviewById(1);

        assertThat(reviewOptional)
                .isPresent()
                .hasValueSatisfying(review ->
                        assertThat(review).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void create() {
        Collection<Review> reviewCollection = reviewStorage.findAll();

        assertEquals(reviewCollection.size(), 1, "Количество обзоров не совпало");
    }

    @Test
    void update() {
        review = new Review(1, "ккккккккк", true, 1, 1, 0);
        reviewStorage.update(review);

        Optional<Review> reviewOptional = reviewStorage.getReviewById(1);

        assertEquals(reviewOptional.get().getContent(), "ккккккккк", "не обновилось");
    }

    @Test
    void removeReview() {
        reviewStorage.removeReview(1);

        Collection<Review> reviewCollection = reviewStorage.findAll();

        assertEquals(reviewCollection.size(), 0, "Количество обзоров не совпало");
    }

    @Test
    void putLikeReviewById() {
        reviewStorage.putLikeReviewById(1, 1);

        Optional<Review> reviewOptional = reviewStorage.getReviewById(1);

        assertEquals(reviewOptional.get().getUseful(), 1, "лайк не поставлен");
    }

    @Test
    void putDislikeReviewById() {
        reviewStorage.putDislikeReviewById(1, 1);

        Optional<Review> reviewOptional = reviewStorage.getReviewById(1);

        assertEquals(reviewOptional.get().getUseful(), -1, "дизлайк не поставлен");
    }

    @Test
    void getReviewsFilmById() {
        Collection<Review> reviewCollection = reviewStorage.getReviewsFilmById(null, null);

        assertEquals(reviewCollection.size(), 1, "Количество обзоров не совпало");
    }

    @Test
    void findAll() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);

        Collection<Review> reviewCollection = reviewStorage.findAll();

        assertEquals(reviewCollection.size(), 3, "Количество обзоров не совпало");
    }
}