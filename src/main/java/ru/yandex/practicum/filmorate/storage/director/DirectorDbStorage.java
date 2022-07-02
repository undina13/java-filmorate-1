package ru.yandex.practicum.filmorate.storage.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director addDirector(Director director) {
        log.info("DirectorDbStorage=>addDirector " + director);
        String INSERT_DIRECTOR = "insert into public.directors (name) values (?)";
        log.info("DirectorDbStorage=>addDirector " + INSERT_DIRECTOR);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        log.info("DirectorDbStorage=>addDirector keyHolder" + keyHolder);
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(INSERT_DIRECTOR, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        log.info("DirectorDbStorage=>addDirector после jdbcTemplate.update");
        director.setId(keyHolder.getKey().intValue());
        log.info("DirectorDbStorage=>addDirector " + director);
        return director;
    }

    @Override
    public Optional<Director> getDirector(int id) {
        String DIRECTOR_SELECT = "SELECT * FROM directors WHERE director_id = ?";
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
        String DIRECTORS_SELECT_ALL = "SELECT * FROM directors";
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
        String DIRECTOR_UPDATE = "update directors set name = ? where director_id = ?";
        jdbcTemplate.update(DIRECTOR_UPDATE,
                director.getName(),
                director.getId());

        return director;
    }

    @Override
    public boolean removeDirector(int id) {
        log.info("FilmDbStorage => removeDirector removeDirector " + id);
        String DIRECTOR_DELETE="DELETE FROM DIRECTORS WHERE DIRECTOR_ID=?";
        //String DIRECTOR_DELETE="delete from directors where director_id=?";
        return jdbcTemplate.update(DIRECTOR_DELETE,id)>0;
    }


}
