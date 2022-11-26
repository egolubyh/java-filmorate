package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получить жанр с переданным идентификатором
     * @param id идентификатор жанра
     * @return жанр
     */
    public Optional<Genre> readGenre(long id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE ID = ?";

        return jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class), id).stream().findAny();
    }

    /**
     * Получить список всех жанров имеющиеся в базе данных
     * @return список жанров
     */
    public List<Genre> readAllGenre() {
        String sqlQuery = "SELECT * FROM GENRE";

        return jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class));
    }
}
