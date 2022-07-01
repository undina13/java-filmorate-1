package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String GENRE_GET_SQL = "select * from GENRE where GENRE_ID = ?";
    private final String GENRE_ALL_SQL = "select * from GENRE";

    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GENRE_GET_SQL, id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("name")
            );
            return genre;
        }
        return null;
    }

    public List<Genre> getAll() {
        List<Genre> genres = jdbcTemplate.query(GENRE_ALL_SQL, (rs, rowNum) -> makeGenre(rs));
        return genres;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre(
                rs.getInt("Genre_ID"),
                rs.getString("name"));
        return genre;
    }
}
