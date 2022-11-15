package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;

    private final FilmGenreDbStorage filmGenreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, FilmGenreDbStorage filmGenreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.filmGenreDbStorage = filmGenreDbStorage;
    }

    /**
     * Сохранить фильм в базе данных
     * @param film фильм
     * @return фильм
     */
    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA) " +
                "VALUES ( ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return film;
    }

    /**
     * Получить фильм
     * @param id идентификатор фильма
     * @return фильм
     */
    @Override
    public Film readFilm(long id) {
        String sqlQuery = "SELECT ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA " +
                " FROM FILM WHERE ID = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    /**
     * Получить все имеющиеся фильмы в базе данных
     * @return список фильмов
     */
    @Override
    public List<Film> readAllFilms() {
        String sqlQuery = "SELECT ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, MPA " +
                " FROM FILM";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    /**
     * Обновление информации о фильме
     * @param film фильм с обновленной информацией
     * @return фильм с обновленной информацией
     */
    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE FILM SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ?, MPA = ?" +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        return film;
    }

    /**
     * Удаление записи о фильме
     * @param id фильм который нужно удалить из базы данных
     */
    @Override
    public void deleteFilm(long id) {
        String sqlQuery = "DELETE FROM FILM WHERE ID = ?";

        jdbcTemplate.update(sqlQuery, id);
    }

    /**
     * Проверяет существование идентификатора
     * @param id идентификатор фильма
     * @return результат условия
     */
    @Override
    public boolean idNotExist(long id) {
        String sqlQuery = "SELECT EXISTS(SELECT * FROM FILM WHERE ID = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("ID");
        long mpaId = rs.getLong("MPA");

        Film film = new Film();
        film.setId(filmId);
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASEDATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setRate(rs.getInt("RATE"));
        film.setMpa(mpaDbStorage.readMpa(mpaId));
        film.setGenres(filmGenreDbStorage.readAllFilmGenre(filmId));

        return film;
    }
}
