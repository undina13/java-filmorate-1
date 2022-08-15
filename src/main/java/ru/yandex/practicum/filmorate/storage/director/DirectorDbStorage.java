package ru.yandex.practicum.filmorate.storage.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DirectorDbStorage implements DirectorStorage {
    private final String INSERT_DIRECTOR = "insert into public.directors (name) values (?)";
    private final String DIRECTOR_SELECT = "SELECT * FROM directors WHERE director_id = ?";
    private final String DIRECTORS_SELECT_ALL = "SELECT * FROM directors";
    private final String DIRECTOR_UPDATE = "update directors set name = ? where director_id = ?";
    private final String DIRECTOR_DELETE = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID=?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director addDirector(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_DIRECTOR, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return director;
    }

    @Override
    public Optional<Director> getDirector(int id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(DIRECTOR_SELECT, id);
        if (directorRows.next()) {
            Director director = new Director(
                    directorRows.getInt("director_id"),
                    directorRows.getString("name")
            );

            return Optional.of(director);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        List<Director> allDirectors = new ArrayList<>();
        SqlRowSet directorsRows = jdbcTemplate.queryForRowSet(DIRECTORS_SELECT_ALL);
        while (directorsRows.next()) {
            Director director = new Director(
                    directorsRows.getInt("director_id"),
                    directorsRows.getString("name")
            );
            allDirectors.add(director);
        }
        return allDirectors;
    }

    @Override
    public Director updateDirector(Director director) {
        jdbcTemplate.update(DIRECTOR_UPDATE,
                director.getName(),
                director.getId());
        return director;
    }

    @Override
    public boolean removeDirector(int id) {
        return jdbcTemplate.update(DIRECTOR_DELETE, id) > 0;
    }
}
