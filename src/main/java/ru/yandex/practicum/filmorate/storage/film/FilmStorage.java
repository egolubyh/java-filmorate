package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film readFilm(long id);

    List<Film> readAllFilms();

    List<Film> findRecommendedFilms(long id);

    Film updateFilm(Film film);

    void deleteFilm(long id);

    boolean idNotExist(long id);

    boolean idDirectorNotExist(long id);
}
