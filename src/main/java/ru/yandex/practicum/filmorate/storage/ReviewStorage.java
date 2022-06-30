package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Optional;

@Component
public class ReviewStorage {
    private static int idCounter = 1;
    private final String REVIEW_DELETE_SQL = "DELETE FROM REVIEWS WHERE review_id=?";
    private final String REVIEW_UPDATE_SQL = "UPDATE REVIEWS SET content=?,is_positive=? WHERE review_id=?";
    private final String REVIEW_INSERT_SQL = "INSERT INTO REVIEWS (content,is_positive,user_id,film_id) " +
            "VALUES(?,?,?,?)";
    private final String REVIEW_BY_ID_SQL = "SELECT * FROM REVIEWS WHERE review_id = ?";
    private final String USER_BY_ID_SQL = "SELECT * FROM USERS WHERE user_id = ?";
    private final String FILM_BY_ID_SQL = "SELECT * FROM FILM WHERE film_id = ?";
    private final String REVIEW_LIKE_BY_ID_SQL = "UPDATE REVIEWS SET useful=useful+1 WHERE review_id=?";
    private final String REVIEW_DISLIKE_BY_ID_SQL = "UPDATE REVIEWS SET useful=useful-1 WHERE review_id=?";
    private final String REVIEWS_POPULAR_BY_ID_SQL = "SELECT * FROM REVIEWS r LEFT JOIN FILM f ON" +
            " r.film_id=f.film_id  WHERE f.film_id=? LIMIT ?";
    private final String REVIEWS_ALL_SQL = "SELECT * FROM REVIEWS";
    private final String REVIEWS_LIMIT_SQL = "SELECT * FROM REVIEWS LIMIT ?";
    private final Logger log = LoggerFactory.getLogger(ReviewStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    @Autowired
    public ReviewStorage(JdbcTemplate jdbcTemplate, EventStorage eventStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventStorage = eventStorage;
    }

    public static int getIdCounter() {
        return idCounter++;
    }

    public Optional<Review> getReviewById(Integer reviewId) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL, reviewId);
        if (reviewRows.next()) {
            return Optional.of(jdbcTemplate.queryForObject(REVIEW_BY_ID_SQL, this::makeReview, reviewId));
        } else {
            log.error("Обзор с идентификатором {} не найден.", reviewId);
            throw new ReviewNotFoundException("Такого обзора нет");
        }

    }

    private Review makeReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

    public Review create(Review review) {
        validateReview(review);
        review.setId(getIdCounter());
        log.debug("Обзор записан");
        jdbcTemplate.update(REVIEW_INSERT_SQL,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId());

        eventStorage.createEvent(new Event(0,
                LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                review.getUserId(),
                EventType.REVIEW,
                Operation.ADD,
                review.getId()));
        return review;
    }


    public Review update(Review review) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL, review.getId());
        if (reviewRows.next()) {
            validateReview(review);
            log.debug("Обзор изменен");
            jdbcTemplate.update(REVIEW_UPDATE_SQL,
                    review.getContent(),
                    review.getIsPositive(),
                    review.getId());

            Review review1 = getReviewById(review.getId()).get();
            eventStorage.createEvent(new Event(0,
                    LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                    review1.getUserId(),
                    EventType.REVIEW,
                    Operation.UPDATE,
                    review1.getId()));
            return review;
        } else {
            throw new ReviewNotFoundException("Такого обзора нет");
        }
    }

    private Review validateReview(Review review) {
        if (review.getContent() == null) {
            log.error("Запись обзора не удалась, пустое контент");
            throw new ValidationException("Контент не может быть пустым");
        }
        if (review.getUserId() == 0) {
            log.error("Запись обзора не удалась, пустой пользователь");
            throw new ValidationException("Пользователь не может быть пустым");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(USER_BY_ID_SQL, review.getUserId());
        if (userRows.next()) {
            if (review.getFilmId() == 0) {
                log.error("Запись обзора не удалась, пустой фильм");
                throw new ValidationException("Фильм не может быть пустым");
            }
            SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FILM_BY_ID_SQL, review.getFilmId());
            if (filmRows.next()) {
                if (review.getIsPositive() == null) {
                    log.error("Запись обзора не удалась, тип отзыва пустой");
                    throw new ValidationException("Тип отзыва не может быть пустым");
                }
                return review;
            } else {
                log.error("Запись обзора не удалась, такого фильма нет");
                throw new FilmNotFoundException("Такого фильма нет");
            }
        } else {
            log.error("Запись обзора не удалась, такого фильма нет");
            throw new UserNotFoundException("Такого пользователя нет");
        }
    }

    public void removeReview(Integer reviewId) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL, reviewId);
        if (reviewRows.next()) {
            log.debug("Обзор удален");
            Review review = getReviewById(reviewId).get();
            jdbcTemplate.update(REVIEW_DELETE_SQL, reviewId);
            eventStorage.createEvent(new Event(0,
                    LocalDateTime.now().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli(),
                    review.getUserId(),
                    EventType.REVIEW,
                    Operation.REMOVE,
                    review.getId()));
        } else {
            throw new ReviewNotFoundException("Такого обзора нет");
        }
    }

    public void putLikeReviewById(Integer reviewId, Integer userId) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL, reviewId);
        if (reviewRows.next()) {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(USER_BY_ID_SQL, userId);
            if (userRows.next()) {
                log.debug("Обзору поставили лайк");
                jdbcTemplate.update(REVIEW_LIKE_BY_ID_SQL, reviewId);
            } else {
                throw new UserNotFoundException("Такого пользователя нет");
            }
        } else {
            throw new ReviewNotFoundException("Такого обзора нет");
        }
    }

    public void putDislikeReviewById(Integer reviewId, Integer userId) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL, reviewId);
        if (reviewRows.next()) {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(USER_BY_ID_SQL, userId);
            if (userRows.next()) {
                log.debug("Обзору поставили лайк");
                jdbcTemplate.update(REVIEW_DISLIKE_BY_ID_SQL, reviewId);
            } else {
                throw new UserNotFoundException("Такого пользователя нет");
            }
        } else {
            throw new ReviewNotFoundException("Такого обзора нет");
        }
    }

    public Collection<Review> getReviewsFilmById(Integer filmId, Integer count) {
        if (filmId == null && count == null) {
            return findAll();
        }
        if (filmId != null && count == null) {
            count = 10;
            return jdbcTemplate.query(REVIEWS_POPULAR_BY_ID_SQL, this::makeReview, filmId, count);
        }
        if (filmId == null) {
            return jdbcTemplate.query(REVIEWS_LIMIT_SQL, this::makeReview, count);
        } else {
            return jdbcTemplate.query(REVIEWS_POPULAR_BY_ID_SQL, this::makeReview, filmId, count);
        }
    }

    public Collection<Review> findAll() {
        return jdbcTemplate.query(REVIEWS_ALL_SQL, this::makeReview);
    }
}
