package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.sql.Connection;
import java.sql.*;
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


        if (film.getDirectors() != null) {
            insertFilmAndDirector(film);
        }
        return film;
    }

    public void insertFilmAndDirector(Film film) {
        String sql = "insert into FILM_DIRECTOR values (?, ?)";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Director director : film.getDirectors()) {
                ps.setLong(1, film.getId());
                ps.setLong(2, director.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        deleteDirectorsByFilmId(film.getId());

        if (film.getDirectors() != null) {
            insertFilmAndDirector(film);
        }
        return film;
    }

    public void deleteDirectorsByFilmId(long id) {
        final String sql = "delete from Film_Director " +
                "director_id " +
                "where film_id = ?";
        jdbcTemplate.update(sql, id);
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

    @Override
    public boolean idDirectorNotExist(long id) {
        String sqlQuery = "SELECT EXISTS(SELECT * FROM FILM_DIRECTOR WHERE DIRECTOR_ID = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }

    /**
     * Найти список наиболее популярных фильмов
     * @param count максимальное кол-во фильмов
     * @return список фильма
     */
    public List<Film> findMostPopularFilms(int count) {
        String sql = "SELECT f.* " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKES L on f.ID = L.FILM_ID " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    /**
     * Найти список наиболее популярных фильмов по году
     * @param year год фильма
     * @return список фильмов
     */
    public List<Film> findMostPopularFilmsByYear(int year) {
        String sql = "SELECT f.* " +
                "FROM FILM AS f " +
                "JOIN FILM_GENRE AS FG on f.ID = FG.FILM " +
                "LEFT JOIN LIKES L on f.ID = L.FILM_ID " +
                "WHERE EXTRACT(YEAR FROM f.RELEASEDATE) = ? " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC ";

        return jdbcTemplate.query(sql, this::mapRowToFilm, year);
    }

    /**
     * Найти список наиболее популярных фильмов по жанру
     * @param genreId id жанра
     * @return список фильмов
     */
    public List<Film> findMostPopularFilmsByGenre(long genreId) {
        String sql = "SELECT f.* " +
                "FROM FILM AS f " +
                "JOIN FILM_GENRE AS FG on f.ID = FG.FILM " +
                "LEFT JOIN LIKES L on f.ID = L.FILM_ID " +
                "WHERE FG.GENRE = ? " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC ";

        return jdbcTemplate.query(sql, this::mapRowToFilm, genreId);
    }

    /**
     * Найти список наиболее популярных фильмов по жанру и году
     * @param year год фильма
     * @param genreId жанр фильма
     * @return список фильмов
     */
    public List<Film> findMostPopularFilmsByYearAndGenre(long genreId, int year) {
        String sql = "SELECT f.* " +
                "FROM FILM AS f " +
                "JOIN FILM_GENRE AS FG on f.ID = FG.FILM " +
                "LEFT JOIN LIKES L on f.ID = L.FILM_ID " +
                "WHERE EXTRACT(YEAR FROM f.RELEASEDATE) = ? AND FG.GENRE = ? " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC ";

        return jdbcTemplate.query(sql, this::mapRowToFilm, year, genreId);
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
        film.setDirectors(findDirectorsByFilmId(filmId));
        return film;
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id((int) resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public List<Director> findDirectorsByFilmId(Long id) {
        String sqlQuery  = "select d.id, d.name\n" +
                "from directors as d\n" +
                "inner join film_director as fd on fd.director_id = d.id\n" +
                "inner join film as f on f.id = fd.film_id\n" +
                "where f.id =?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector, id);
    }


    public List<Film> findFilmsByDirectorsIdbyLike(Long id) {

        String sqlQuery  = "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASEDATE,\n" +
                "F.DURATION,F.RATE, F.MPA\n" +
                "FROM FILM F\n" +
                "LEFT JOIN LIKES L ON F.ID = L.FILM_ID\n" +
               "JOIN FILM_DIRECTOR FD ON F.ID = FD.FILM_ID\n" +
                "WHERE FD.DIRECTOR_ID = ?\n" +
               "GROUP BY F.ID \n" +
                " ORDER BY COUNT(L.USER_ID) ";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
    }

    public List<Film> findFilmsByDirectorsIdbyYar(Long id) {

        String sqlQuery  = "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASEDATE,\n" +
                "F.DURATION,F.RATE, F.MPA\n" +
                "                FROM FILM F\n" +
                "                JOIN FILM_DIRECTOR FD ON F.ID = FD.FILM_ID\n" +
                "                WHERE FD.DIRECTOR_ID = ?" +
                "                ORDER BY F.RELEASEDATE ";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
    }
}
