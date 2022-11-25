package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
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


        if (film.getDirectors() != null) {
            insertFilmAndDirector(film);
        }

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

        deleteDirectorsByFilmId(film.getId());

        if (film.getDirectors() != null) {
            insertFilmAndDirector(film);
        }

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
        String sql = "SELECT F.* " +
                "FROM FILM AS F " +
                "JOIN FILM_GENRE AS FG on F.ID = FG.FILM " +
                "LEFT JOIN LIKES L on F.ID = L.FILM_ID " +
                "WHERE EXTRACT(YEAR FROM F.RELEASEDATE) = ? " +
                "GROUP BY F.ID " +
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

    /**
     * Найти список рекомендуемых фильмов
     * @param id идентификатор фильма     *
     * @return список фильмов
     */
    @Override
    public List<Film> findRecommendedFilms(long id) {
        final int usersLimit = 3;
        final String sql =
                "SELECT F2.*, MPA.ID MPA_ID, MPA.NAME MPA_NAME " +
                        "FROM FILM F " +
                        "   JOIN LIKES FL ON F.id = FL.FILM_ID AND FL.USER_ID = ?1 " +
                        "   JOIN LIKES FL2 ON FL.FILM_ID = FL2.FILM_ID AND FL2.USER_ID <> ?1 " +
                        "   JOIN LIKES FL3 ON FL2.USER_ID = FL3.USER_ID AND FL3.USER_ID <> ?1 " +
                        "   LEFT JOIN LIKES FL4 ON FL3.FILM_ID = FL4.FILM_ID AND FL4.USER_ID = ?1 " +
                        "   JOIN FILM F2 ON FL3.FILM_ID = F2.ID " +
                        "   LEFT JOIN MPA ON F2.ID = MPA.ID " +
                        "WHERE FL4.USER_ID IS NULL " +
                        "GROUP BY F2.ID " +
                        "ORDER BY COUNT(FL2.USER_ID) DESC " +
                        "LIMIT ?2";
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
                .directors(findDirectorsByFilmId(filmId))
                .build();
    }

    private Set<Long> getLikesByFilmId(long filmId) {
        String sqlQuery = "SELECT USER_ID FROM LIKES F WHERE F.FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }

    private List<Genre> getGenresByFilmId(long filmId) {
        String sqlQuery = "SELECT * FROM FILM_GENRE FG LEFT JOIN GENRE G ON FG.GENRE = G.ID WHERE FG.FILM = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                new Genre(rs.getLong("id"), rs.getString("name")), filmId);
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id((int) resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public List<Director> findDirectorsByFilmId(Long id) {
        String sqlQuery  = "SELECT D.ID, D.NAME\n" +
                "FROM DIRECTORS AS D\n" +
                "INNER JOIN FILM_DIRECTOR AS FD ON FD.DIRECTOR_ID = D.ID\n" +
                "INNER JOIN FILM AS F ON F.ID = FD.FILM_ID\n" +
                "WHERE F.ID =?";

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

    /**
     * Получить список фильмов по подстроке, поиск по названию фильма
     * @param query подстрока
     * @return список фильмов
     */
    public List<Film> findAllFilmsBySearchTitle(String query) {
        String sqlQuery = "SELECT f.* " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKES l on f.ID = l.FILM_ID " +
                "WHERE f.NAME ILIKE '%" + query + "%' " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    /**
     * Получить список фильмов по подстроке, поиск по режиссеру
     * @param query подстрока
     * @return список фильмов
     */
    public List<Film> findAllFilmsBySearchDirector(String query) {
        String sqlQuery = "SELECT f.* " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKES l on f.ID = l.FILM_ID " +
                "JOIN FILM_DIRECTOR FD on f.ID = FD.FILM_ID " +
                "JOIN DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
                "WHERE D.NAME ILIKE '%" + query + "%' " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    /**
     * Получить список фильмов по подстроке, поиск по названию фильма и режиссеру
     * @param query подстрока
     * @return список фильмов
     */
    public List<Film> findAllFilmsBySearchDirectorAndTitle(String query) {
        String sqlQuery = "SELECT f.* " +
                "FROM FILM AS f " +
                "LEFT JOIN LIKES l on f.ID = l.FILM_ID " +
                "LEFT JOIN FILM_DIRECTOR FD on f.ID = FD.FILM_ID " +
                "LEFT JOIN DIRECTORS D on D.ID = FD.DIRECTOR_ID " +
                "WHERE D.NAME ILIKE '%" + query + "%' " +
                "OR f.NAME ILIKE '%" + query + "%' " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.USER_ID) DESC";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    public void deleteDirectorsByFilmId(long id) {
        String sql = "DELETE FROM FILM_DIRECTOR WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
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



    public List<Film> GetFilmsLikedByUser(Long userId) {

        String sqlQuery  ="select f.id, f.name, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.Rate, f.MPA\n" +
                "from film as f\n" +
                "LEFT JOIN LIKES l ON f.ID = l.FILM_ID\n" +
                "Where l.USER_ID = ?\n" +
                "GROUP BY f.ID\n" +
                "ORDER BY COUNT(l.USER_ID) DESC";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, userId);

    }
}
