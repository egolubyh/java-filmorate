package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {


    private final FilmStorage filmStorage;
    private final LikeDbStorage likeDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikeDbStorage likeDbStorage, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
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
            genreDbStorage.deleteFilmGenre(film.getId());
            genreDbStorage.createFilmGenre(film.getId(), list);
        }
    }

    private void setMpa(Film film) {
        Mpa mpa = mpaDbStorage.readMpa(film.getMpa().getId());
        film.setMpa(mpa);
    }

}
