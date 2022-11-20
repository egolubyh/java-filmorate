package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            PreparedStatement ps = connection.prepareStatement(sqlQuery,  Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue(); Objects.requireNonNull(keyHolder.getKey()).longValue();
        director.setId((int) id);
        return director;
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



    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id((int) resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

}
