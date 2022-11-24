package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ReviewDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получение всех обзоров
     */
    public List<Review> readAllReview(Integer count) {
        String sqlQuery = "SELECT * " +
                " FROM REVIEWS ORDER BY USEFUL DESC LIMIT ?";
        log.info("Выводятся все обзоры на все фильмы");
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, count);
    }

    /**
     * Получение всех обзоров на определённый фильм
     */
    public List<Review> readAllReviewForFilm(Long id, Integer count) {
        String sqlQuery = "SELECT * " +
                " FROM REVIEWS WHERE FILM_ID = ? ORDER BY USEFUL DESC LIMIT ?";
        log.info("Выводятся все обзоры на фильм с id = {}", id);
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, id, count);
    }

    /**
     * Получение обзора по id
     */
    public Review readReview(long id, Integer count) throws NotFoundException {
        String sqlQuery = "SELECT * " +
                " FROM REVIEWS WHERE ID = ? LIMIT ?";
        Review review;
        try {
            review = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, id, count);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(id, "Не найден отзыв");
        }
        log.info("Выводится обзор с id = {}", id);
        return review;
    }

    /**
     * Получение обзора по id пользователя и id фильма
     */
    public Review readReviewByUserIdAndFilmId(Long userId, Long filmId) throws NotFoundException {
        String sqlQuery = "SELECT * " +
                " FROM REVIEWS WHERE USER_ID = ? AND  FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, userId, filmId);
    }

    /**
     * Создание обзора
     */
    public Review createReview(Review review) throws NotFoundException {
        String sqlQuery = "INSERT INTO REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) " +
                "VALUES ( ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.isPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            stmt.setLong(5, review.getUseful());
            return stmt;
        }, keyHolder);

        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Создан обзор с id {} на фильм с id {}", review.getReviewId(), review.getFilmId());
        return review;
    }

    /**
     * Обновление обзора
     */
    public Review updateReview(Review review) throws NotFoundException {
        String sqlQuery = "UPDATE REVIEWS SET " +
                "CONTENT = ?, IS_POSITIVE = ?, USEFUL = ?" +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.isPositive(),
                0,
                review.getReviewId());
        log.info("Обновлён обзор с id {} на фильм с id {}", review.getReviewId(), review.getFilmId());
        return review;
    }

    /**
     * Удаление обзора
     */
    public void deleteReview(Long id) {
        String sqlQuery = "DELETE FROM REVIEWS WHERE ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.info("Удалён обзор с id {}", id);
    }

    /**
     * Добавление лайков/дизлайков обзорам
     */
    public void addLikeToReview(Long id) {
        String sqlQuery = "UPDATE REVIEWS SET USEFUL = USEFUL + 1 WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.info("Добавлен лайк обзору с id = {}", id);
    }

    public void addDislikeToReview(Long id) {
        String sqlQuery = "UPDATE REVIEWS SET USEFUL = USEFUL - 1 WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
        log.info("Добавлен дизлайк обзору с id = {}", id);
    }

    /**
     * Сборка обзора из мапы
     */
    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review(rs.getLong("ID")
                , rs.getString("CONTENT")
                , rs.getBoolean("IS_POSITIVE")
                , rs.getLong("USER_ID")
                , rs.getLong("FILM_ID")
                , rs.getLong("USEFUL"));
        return review;
    }
}
