package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewStorageTest {               //Тесты запускать по очереди !!!
    private final ReviewStorage reviewStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private Review review;
    private Film film;
    private User user;

    @BeforeEach
    void setUp() {
        film = new Film(1,"tort","test",LocalDate.of(2010,2,2),100,new MPAA(5,"NC-17"));
        filmDbStorage.create(film);
        user = new User(1, "tortis", "dmitry", "tortiss00@yandex.ru",
                LocalDate.of(1992, 4, 23));
        userDbStorage.create(user);
    }

    @Test
    void getReviewById() {

        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);

        Optional<Review> reviewOptional=reviewStorage.getReviewById(1);

        assertThat(reviewOptional)
                .isPresent()
                .hasValueSatisfying(review ->
                        assertThat(review).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void create() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);

        Collection<Review> reviewCollection=reviewStorage.findAll();

        assertEquals(reviewCollection.size(), 1, "Количество обзоров не совпало");

    }

    @Test
    void update() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        review = new Review(1, "ккккккккк", true, 1, 1, 0);
        reviewStorage.update(review);

        Optional<Review> reviewOptional=reviewStorage.getReviewById(1);

        assertEquals(reviewOptional.get().getContent(), "ккккккккк", "не обновилось");

    }

    @Test
    void removeReview() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        reviewStorage.removeReview(1);

        Collection<Review> reviewCollection=reviewStorage.findAll();

        assertEquals(reviewCollection.size(), 0, "Количество обзоров не совпало");

    }

    @Test
    void putLikeReviewById() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        reviewStorage.putLikeReviewById(1,1);

        Optional<Review> reviewOptional=reviewStorage.getReviewById(1);

        assertEquals(reviewOptional.get().getUseful(), 1, "лайк не поставлен");

    }

    @Test
    void putDislikeReviewById() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        reviewStorage.putDislikeReviewById(1,1);

        Optional<Review> reviewOptional=reviewStorage.getReviewById(1);

        assertEquals(reviewOptional.get().getUseful(), -1, "дизлайк не поставлен");


    }

    @Test
    void getReviewsFilmById() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        Collection<Review> reviewCollection=reviewStorage.getReviewsFilmById(null,null);

        assertEquals(reviewCollection.size(), 1, "Количество обзоров не совпало");


    }

    @Test
    void findAll() {
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);
        review = new Review(1, "test", true, 1, 1, 0);
        reviewStorage.create(review);

        Collection<Review> reviewCollection=reviewStorage.findAll();

        assertEquals(reviewCollection.size(), 3, "Количество обзоров не совпало");
    }
}