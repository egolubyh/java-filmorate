package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmStorage {

    private final static String GET_GENRES_BY_FILM_ID_SQL = "select * from film_genre fg "
            + "left join genre g on fg.genre = g.id "
            + "where fg.film = ?";

    private final static String GET_DIRECTORS_BY_FILM_ID_SQL = "select * from film_directors fd "
            + "left join directors d on fd.director_id = d.director_id "
            + "where fd.film_id = ?";
    private final static String GET_LIKES_BY_FILM_ID_SQL = "select user_id from likes f where f.film_id = ?";


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
            stmt.setLong(4, film.getDuration());
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

    @Override
    public List<Film> findRecommendedFilms(long id) {
        final int usersLimit = 3;
        final String sql =
                "select f2.*, mpa.id  mpa_id, mpa.name  mpa_name " +
                        "from film f " +
                        "   join likes fl on f.id = fl.film_id and fl.user_id = ?1 " +
                        "   join likes fl2 on fl.film_id = fl2.film_id and fl2.user_id <> ?1 " +
                        "   join likes fl3 on fl2.user_id = fl3.user_id and fl3.user_id <> ?1 " +
                        "   left join likes fl4 on fl3.film_id = fl4.film_id and fl4.user_id = ?1 " +
                        "   join film f2 on fl3.film_id = f2.id " +
                        "   left join mpa on f2.id = mpa.id " +
                        "where fl4.user_id is null " +
                        "group by f2.id " +
                        "order by count(fl2.user_id) desc " +
                        "limit ?2";
        return jdbcTemplate.query(sql, this::mapRowToFilm, id, usersLimit);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("ID");
        long mpaId = rs.getLong("MPA");

        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getLong("duration"))
                .likes(getLikesByFilmId(filmId))
                .rate(rs.getInt("rate"))
                .mpa(mpaDbStorage.readMpa(mpaId))
                .genres(getGenresByFilmId(filmId))
                .directors(getDirectorsByFilmId(filmId))
                .build();
    }

    private Set<Long> getLikesByFilmId(long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_LIKES_BY_FILM_ID_SQL,
                (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }

    private Set<Genre> getGenresByFilmId(long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_GENRES_BY_FILM_ID_SQL, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")), filmId));
    }

    private Set<Director> getDirectorsByFilmId(long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_DIRECTORS_BY_FILM_ID_SQL, (rs, rowNum) ->
                new Director(rs.getLong("director_id"), rs.getString("name")), filmId));
    }
}
