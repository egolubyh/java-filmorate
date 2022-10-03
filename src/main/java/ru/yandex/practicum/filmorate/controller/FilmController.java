package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final InMemoryFilmStorage filmStorage;
    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /films, метод POST");
        if (!ValidationService.valid(film)) {
            log.debug("Ошибка");
            throw new ValidationException();
        }

        return filmStorage.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /films, метод PUT");
        if (!filmStorage.filmIsExist(film.getId()) || !ValidationService.valid(film)) {
            log.debug("Ошибка");
            throw new ValidationException();
        }

        return filmStorage.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        log.info("Получен запрос к эндпоинту: /films, метод GET");

        return filmStorage.findAll();
    }
}
