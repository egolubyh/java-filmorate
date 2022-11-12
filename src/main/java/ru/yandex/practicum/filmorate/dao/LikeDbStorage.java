package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) " +
                "VALUES ( ?, ? )";
        jdbcTemplate.update(sqlQuery, userId, filmId );
    }

    public List<Long> findMostPopularId(int count) {
        String sqlQuery = "SELECT f.ID AS FILM_ID " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKES l ON f.ID = l.FILM_ID " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("FILM_ID"), count);
    }

    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM LIKES " +
                "WHERE USER_ID = ? " +
                "AND FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId );
    }
}
