package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.springframework.jdbc.support.KeyHolder;;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
public class DirectorStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DirectorStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Director addDirector(Director director)  {
        String sqlQuery = "insert into DIRECTORS(NAME) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery,   new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue(); Objects.requireNonNull(keyHolder.getKey()).longValue();
        director.setId((int) id);
        return director;
    }

    public Director updateDirector(Director director) throws NotFoundException {
        String sqlQuery = "update Directors set NAME = ?" +
                "  where ID = ?";
        jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId());
        return findDirectorById((long) director.getId());
    }

    public Director findDirectorById(Long id) throws NotFoundException {
        String sqlQuery = "select * from Directors where id = ?";
        Director director;
        try {
            director = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(id, "Фильм с id=%d не найден.");
        }
        return director;
    }

    public List<Director> findAllDirectors() {
        String sqlQuery = "select * from directors";
        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id((int) resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public void deleteDirectorsById(long id) {
        final String sql = "delete from directors where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
