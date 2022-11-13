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

    /**
     * Добавить жанры указанного фильма в базу данных
     * @param filmId идентификатор фильма жанры которого нужно добавить
     * @param genres список жанров
     */
    public void createFilmGenre(long filmId, List<Genre> genres) {
        String sqlQuery = "INSERT INTO FILM_GENRE (FILM, GENRE) " +
                "VALUES ( ?, ? )";

        genres.stream()
                .distinct()
                .forEach(g -> jdbcTemplate.update(sqlQuery, filmId, g.getId()));
    }

    /**
     * Получить жанр с переданным идентификатором
     * @param id идентификатор жанра
     * @return жанр
     */
    public Genre readGenre(long id) {
        String sqlQuery = "SELECT ID, TITLE AS NAME " +
                " FROM GENRE WHERE ID = ?";

        return jdbcTemplate.queryForObject(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class), id);
    }

    /**
     * Получить список всех жанров имеющиеся в базе данных
     * @return список жанров
     */
    public List<Genre> readAllGenre() {
        String sqlQuery = "SELECT ID, TITLE AS NAME " +
                "FROM GENRE ";

        return jdbcTemplate.query(sqlQuery,
                new BeanPropertyRowMapper<>(Genre.class));
    }

    /**
     * Получить все жанры определенного фильма
     * @param filmId идентификатор фильма
     * @return список жанров
     */
    public List<Genre> readAllGenre(long filmId) {
        String sqlQuery = "SELECT GENRE AS ID, g.TITLE AS NAME " +
                "FROM FILM_GENRE AS fg " +
                "JOIN GENRE AS G on G.ID = fg.GENRE " +
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

    /**
     * Проверяет существование идентификатора
     * @param id идентификатор жанра
     * @return результат условия
     */
    public boolean idNotExist(long id) {
        if (id <= 0) return true;
        String sqlQuery = "SELECT EXISTS(SELECT * FROM GENRE WHERE ID = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }


}
