package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilmById(filmId);

        film.getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilmById(filmId);

        film.getLikes().remove(userId);
    }

    public List<Film> findMostPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> (o1.getLikes().size() - o2.getLikes().size()) * (-1))
                .limit(count)
                .collect(Collectors.toList());
    }

}
