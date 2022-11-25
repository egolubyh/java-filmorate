package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private int id;

    @Override
    public Film createFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film readFilm(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> readAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean idNotExist(long id) {
        return !films.containsKey(id);
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public void deleteFilm(long id) {
        films.remove(id);
    }

    @Override
    public List<Film> findRecommendedFilms(long id) {
        throw new UnsupportedOperationException("Не относится к inMemory");
    }

    @Override
    public boolean idDirectorNotExist(long id) {
        throw new UnsupportedOperationException("Не относится к inMemory");
    }

}
