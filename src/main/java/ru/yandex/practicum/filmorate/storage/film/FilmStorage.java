package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film readFilm(long id);

    List<Film> readAllFilms();

    Film updateFilm(Film film);

    void deleteFilm(long id);

    void deleteAllFilms();

    boolean idNotExist(long id);

    boolean idDirectorNotExist(long id);

    List<Film> findRecommendedFilms(long id);
}
