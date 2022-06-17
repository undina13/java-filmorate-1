package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MPAADbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MPAADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPAA getById(int id){
        SqlRowSet mpaaRows = jdbcTemplate.queryForRowSet("select * from MPAA where MPAA_ID = ?", id);

        if(mpaaRows.next()) {
            MPAA mpaa = new MPAA(
                    mpaaRows.getInt("mpaa_id"),
                    mpaaRows.getString("name")
            );

            return mpaa;
        } else {
            return null;
        }
    }

    public List<MPAA> getAll() {
        String sql = "select * from MPAA" ;

        List<MPAA> mpaas= jdbcTemplate.query(sql, (rs, rowNum) -> makeMpaa(rs));

        return mpaas;
    }

    private MPAA makeMpaa(ResultSet rs) throws SQLException {
        MPAA mpaa = new MPAA(
                rs.getInt("MPAA_ID"),
                rs.getString("name"));

        return mpaa;
    }
}