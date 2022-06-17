package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPAA;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getById(int id){
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE where GENRE_ID = ?", id);
        if(genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("name")
            );
            return genre;
        }

       return null;
    }

    public List<Genre> getAll() {
        String sql = "select * from GENRE" ;

        List<Genre> genres= jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));

        return genres;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre(
                rs.getInt("Genre_ID"),
                rs.getString("name"));

        return genre;
    }
}
