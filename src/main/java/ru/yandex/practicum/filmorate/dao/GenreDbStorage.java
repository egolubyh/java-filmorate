package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createFilmGenre(long filmId, List<Genre> genres) {
        String sqlQuery = "INSERT INTO FILM_GENRE (FILM, GENRE) " +
                "VALUES ( ?, ? )";

        genres.stream()
                .distinct()
                .forEach(g -> jdbcTemplate.update(sqlQuery, filmId, g.getId()));
    }

    public Genre readGenre(long id) {
        String sqlQuery = "SELECT ID, TITLE AS NAME " +
                " FROM GENRE WHERE ID = ?";

        return jdbcTemplate.queryForObject(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class), id);
    }

    public List<Genre> readAllGenre() {
        String sqlQuery = "SELECT ID, TITLE AS NAME " +
                "FROM GENRE ";

        return jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class));
    }

    public List<Genre> readAllGenre(long filmId) {
        String sqlQuery = "SELECT GENRE AS ID, g.TITLE AS NAME " +
                "FROM FILM_GENRE AS fg " +
                "JOIN GENRE AS G on G.ID = fg.GENRE " +
                "WHERE FILM = ?";

        return jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class), filmId);
    }

    public void deleteFilmGenre(long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE " +
                "WHERE FILM = ?";
        jdbcTemplate.update(sqlQuery,filmId);
    }

    public boolean idNotExist(long id) {
        if (id <= 0) return true;
        String sqlQuery = "SELECT EXISTS(SELECT * FROM GENRE WHERE ID = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }


}
