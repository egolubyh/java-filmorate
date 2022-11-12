package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film readFilm(long id);

    List<Film> readAllFilms();

    Film updateFilm(Film film);

    Film deleteFilm(Film film);

    boolean idNotExist(long id);
}
