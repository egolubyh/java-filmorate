package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmDbStorage filmStorage;
    private final LikeDbStorage likeDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final FilmGenreDbStorage filmGenreDbStorage;

    private final DirectorStorage directorStorage;
    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmDbStorage filmStorage,
                       LikeDbStorage likeDbStorage, MpaDbStorage mpaDbStorage, FilmGenreDbStorage filmGenreDbStorage,DirectorStorage directorStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.filmGenreDbStorage = filmGenreDbStorage;
        this.directorStorage=directorStorage;
    }

    /**
     * Добавить лайк фильму.
     * @param filmId идентификатор фильма.
     * @param userId идентификатор пользователя.
     */
    public void addLike(long filmId, long userId) {
        likeDbStorage.createLike(filmId,userId);
    }

    /**
     * Удалить лайк фильму.
     * @param filmId идентификатор фильма.
     * @param userId идентификатор пользователя.
     */
    public void deleteLike(long filmId, long userId) {
        likeDbStorage.deleteLike(filmId, userId);
    }

    /**
     * Добавить фильм в БД.
     * @param film фильм.
     * @return возвращает фильм с присвоенным id.
     */
    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        setMpa(film);
        setGenres(film);

        return film;
    }

    /**
     * Обновить информацию о фильме.
     * @param film фильм.
     * @return возвращает фильм
     */
    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        setMpa(film);
        setGenres(film);

        return film;
    }

    /**
     * Получить список популярных фильмов.
     * @param allParams Параметры запроса: count - максимальное кол-во возвращаемых фильмов,
     * genreId - id жанр фильма, year - год релиза фильма.
     * @return список фильмов.
     */
    public List<Film> findMostPopular(Map<String, String> allParams) {
        if (allParams.isEmpty()) {
            return filmStorage.findMostPopularFilms(10);
        } else if (allParams.containsKey("count")) {
            return filmStorage.findMostPopularFilms(
                    Integer.parseInt(allParams.get("count")));
        } else if (allParams.containsKey("genreId") && allParams.containsKey("year")) {
            return filmStorage.findMostPopularFilmsByYearAndGenre(
                    Long.parseLong(allParams.get("genreId")),
                    Integer.parseInt(allParams.get("year")));
        } else if (allParams.containsKey("genreId")) {
            return filmStorage.findMostPopularFilmsByGenre(
                    Long.parseLong(allParams.get("genreId")));
        } else {
            return filmStorage.findMostPopularFilmsByYear(
                    Integer.parseInt(allParams.get("year")));
        }
    }

    private void setGenres(Film film) {
        if (film.getGenres() != null) {
            List<Genre> list = film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            film.setGenres(list);
            filmGenreDbStorage.deleteFilmGenre(film.getId());
            filmGenreDbStorage.createFilmGenre(film.getId(),
                    list.stream()
                    .map(Genre::getId)
                    .collect(Collectors.toList()));
        }
    }

    private void setMpa(Film film) {
        Mpa mpa = mpaDbStorage.readMpa(film.getMpa().getId());
        film.setMpa(mpa);
    }


    public List<Film> findFilmsByDirectorsId(Long id, String sort)  {

        if (sort.equals("likes")) {
            log.info("getListFilmsByDirectorSortLikes");
            return filmStorage.findFilmsByDirectorsIdbyLike(id);
        }
        else  log.info("getListFilmsByDirectorSortYear");
        return filmStorage.findFilmsByDirectorsIdbyYar(id);
    }

    /**
     * Получить список фильмов по подстроке
     * @param query подстрока
     * @param by где искать
     * @return список фильмов
     */
    public List<Film> findFilmsBySearch(String query, List<String> by) {
        List<Film> films = null;
        if (by.contains("director") && by.contains("title")) {
            films = filmStorage.findAllFilmsBySearchDirectorAndTitle(query);
        } else if (by.get(0).equals("director")) {
            films = filmStorage.findAllFilmsBySearchDirector(query);
        } else if (by.get(0).equals("title")) {
            films = filmStorage.findAllFilmsBySearchTitle(query);
        }

        return films;
    }

    public List<Film> GetCommonFilms(Long userId, Long friendId) {
        List<Film> UserLikedFilms =  filmStorage.GetFilmsLikedByUser(userId);
        List<Film> friendLikedFilms = filmStorage.GetFilmsLikedByUser(friendId);
        log.debug("Общие друзья найдены.");
        return UserLikedFilms.stream()
                .filter(friendLikedFilms::contains)
                .collect(Collectors.toList());
    }
}
