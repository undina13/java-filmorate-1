package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Component
public class ReviewStorage {
    private static int idCounter = 1;
    private final String REVIEW_DELETE_SQL="DELETE FROM REVIEWS WHERE review_id=?";
    private final String REVIEW_UPDATE_SQL="UPDATE REVIEWS SET content=?,is_positive=?,user_id=?,film_id=?,useful=? WHERE review_id=?";
    private final String REVIEW_INSERT_SQL = "INSERT INTO REVIEWS (content,is_positive,user_id,film_id) VALUES(?,?,?,?)";
    private final String REVIEW_BY_ID_SQL = "SELECT * FROM REVIEWS WHERE review_id = ?";
    private final String USER_BY_ID_SQL="SELECT * FROM USERS WHERE user_id = ?";
    private final String REVIEW_LIKE_BY_ID_SQL="UPDATE REVIEWS SET useful=useful+1 WHERE review_id=?";
    private final String REVIEW_DISLIKE_BY_ID_SQL="UPDATE REVIEWS SET useful=useful-1 WHERE review_id=?";
    private final String REVIEWS_POPULAR_BY_ID_SQL="SELECT * FROM REVIEWS r LEFT JOIN FILM f ON r.film_id=f.film_id WHERE f.film_id=? LIMIT ?";
    private final String REVIEWS_ALL_SQL="SELECT * FROM REVIEWS";
    private final Logger log = LoggerFactory.getLogger(ReviewStorage.class);
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    public ReviewStorage(UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
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
            throw new FilmNotFoundException("Такого обзора нет");
        }

    }

    private Review makeReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

    public Review create(Review review) {
        review.setReviewId(getIdCounter());
        log.debug("Обзор записан");
        jdbcTemplate.update(REVIEW_INSERT_SQL,
                review.getContent(),
                review.isPositive(),
                review.getUserId(),
                review.getFilmId());
        return review;
    }


    public Review update(Review review) {
        SqlRowSet reviewRows=jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL,review.getReviewId());
        if (reviewRows.next()){
            log.debug("Обзор изменен");
            jdbcTemplate.update(REVIEW_UPDATE_SQL,
                    review.getContent(),
                    review.isPositive(),
                    review.getUserId(),
                    review.getFilmId(),
                    review.getUseful(),
                    review.getReviewId());
            return review;
        }else {
            throw new FilmNotFoundException("Такого обзора нет");
        }
    }

    public void removeReview(Integer reviewId) {
        SqlRowSet reviewRows=jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL,reviewId);
        if (reviewRows.next()){
            log.debug("Обзор удален");
jdbcTemplate.update(REVIEW_DELETE_SQL,reviewId);
        }else {
            throw new FilmNotFoundException("Такого обзора нет");
        }
    }

    public void putLikeReviewById(Integer reviewId, Integer userId) {
        SqlRowSet reviewRows=jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL,reviewId);
        if (reviewRows.next()){
            SqlRowSet userRows=jdbcTemplate.queryForRowSet(USER_BY_ID_SQL,userId);
            if(userRows.next()){
                log.debug("Обзору поставили лайк");
                jdbcTemplate.update(REVIEW_LIKE_BY_ID_SQL,reviewId);
            }else {
                throw new UserNotFoundException("Такого пользователя нет");
            }
        }else {
            throw new FilmNotFoundException("Такого обзора нет");
        }
    }

    public void putDislikeReviewById(Integer reviewId, Integer userId) {
        SqlRowSet reviewRows=jdbcTemplate.queryForRowSet(REVIEW_BY_ID_SQL,reviewId);
        if (reviewRows.next()){
            SqlRowSet userRows=jdbcTemplate.queryForRowSet(USER_BY_ID_SQL,userId);
            if(userRows.next()){
                log.debug("Обзору поставили лайк");
                jdbcTemplate.update(REVIEW_DISLIKE_BY_ID_SQL,reviewId);
            }else {
                throw new UserNotFoundException("Такого пользователя нет");
            }
        }else {
            throw new FilmNotFoundException("Такого обзора нет");
        }
    }

    public Collection<Review> getReviewsFilmById(Integer count) {
        if (count == null) {
            count = 10;
            return jdbcTemplate.query(REVIEWS_POPULAR_BY_ID_SQL, this::makeReview, count);
        } else {
            return jdbcTemplate.query(REVIEWS_POPULAR_BY_ID_SQL, this::makeReview, count);
        }
    }

    public Collection<Review> findAll() {
return jdbcTemplate.query(REVIEWS_ALL_SQL,this::makeReview);
    }
}
