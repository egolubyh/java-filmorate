package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FilmService {


    private final FilmStorage filmStorage;
    private final LikeDbStorage likeDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       LikeDbStorage likeDbStorage) {
        this.filmStorage = filmStorage;
        this.likeDbStorage = likeDbStorage;
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
     * Получить список популярных фильмов.
     * @param count длинна списка.
     * @return список фильмов.
     */
    public List<Film> findMostPopularFilms(int count) {
        return likeDbStorage.findMostPopularId(count).stream()
                .map(filmStorage::readFilm)
                .collect(Collectors.toList());
    }

}
