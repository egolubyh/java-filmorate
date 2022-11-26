package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получить рейтинг
     * @param id идентификатор рейтинга
     * @return рейтинг
     */
    public Mpa readMpa(long id) {
        String sqlQuery = "SELECT * FROM MPA WHERE ID = ?";

        return jdbcTemplate.queryForObject(sqlQuery,
                new BeanPropertyRowMapper<>(Mpa.class), id);
    }

    /**
     * Получить список всех рейтингов
     * @return список рейтингов
     */
    public List<Mpa> readAllMpa() {
        String sqlQuery = "SELECT * FROM MPA";

        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Mpa.class));
    }

    /**
     * Проверяет существование идентификатора
     * @param id идентификатор рейтинга
     * @return результат условия
     */
    public boolean idNotExist(long id) {
        if (id <= 0) return true;
        String sqlQuery = "SELECT EXISTS(SELECT * FROM MPA WHERE ID = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }
}