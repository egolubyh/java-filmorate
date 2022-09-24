package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filmorate")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping(value = "/film")
    public Film createFilm(@RequestBody Film film) {
        return films.put(film.getId(), film);
    }

    @PutMapping(value = "film")
    public Film updateFilm(@RequestBody Film film) {
        return films.put(film.getId(), film);
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }
}
