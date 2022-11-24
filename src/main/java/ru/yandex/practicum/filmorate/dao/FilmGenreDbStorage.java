package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class FilmGenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавить жанры указанного фильма в базу данных
     * @param filmId идентификатор фильма жанры которого нужно добавить
     * @param genres список жанров
     */

    public void createFilmGenre(long filmId, List<Long> genres) {
        String sqlQuery = "INSERT INTO FILM_GENRE (FILM, GENRE) " +
                "VALUES ( ?, ? )";

        genres.stream()
                .distinct()
                .forEach(g -> jdbcTemplate.update(sqlQuery, filmId, g));
    }

    /**
     * Получить все жанры определенного фильма
     * @param filmId идентификатор фильма
     * @return список жанров
     */

    public List<Genre> readAllFilmGenre(long filmId) {
        String sqlQuery = "SELECT GENRE AS ID, G.NAME AS NAME " +
                "FROM FILM_GENRE AS FG " +
                "JOIN GENRE AS G on G.ID = FG.GENRE " +
                "WHERE FILM = ?";

        return jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class), filmId);
    }

    /**
     * Удалить запись о жанре фильма
     * @param filmId идентификатор фильма
     */
    public void deleteFilmGenre(long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE " +
                "WHERE FILM = ?";
        jdbcTemplate.update(sqlQuery,filmId);
    }
}
