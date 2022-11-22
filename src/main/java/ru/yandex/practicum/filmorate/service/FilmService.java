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
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.SQLException;
import java.util.List;
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
        this. directorStorage=directorStorage;
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
     * @param count длинна списка.
     * @return список фильмов.
     */
    public List<Film> findMostPopularFilms(int count) {
        return likeDbStorage.findMostPopularId(count).stream()
                .map(filmStorage::readFilm)
                .collect(Collectors.toList());
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

if (sort == "likes") {

    return filmStorage.findFilmsByDirectorsIdbyLike(id);
}
else return filmStorage.findFilmsByDirectorsIdbyYar(id);
    }
}
