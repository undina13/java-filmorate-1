package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPAA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MPAADbStorage {
    private final String MPA_GET_SQL = "select * from MPAA where MPAA_ID = ?";
    private final String MPA_ALL_SQL = "select * from MPAA";
    private final JdbcTemplate jdbcTemplate;

    public MPAADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPAA getById(int id) {
        SqlRowSet mpaaRows = jdbcTemplate.queryForRowSet(MPA_GET_SQL, id);
        if (mpaaRows.next()) {
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
        List<MPAA> mpaas = jdbcTemplate.query(MPA_ALL_SQL, (rs, rowNum) -> makeMpaa(rs));
        return mpaas;
    }

    private MPAA makeMpaa(ResultSet rs) throws SQLException {
        MPAA mpaa = new MPAA(
                rs.getInt("MPAA_ID"),
                rs.getString("name"));
        return mpaa;
    }
}
