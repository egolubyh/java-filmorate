package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;
    private final InMemoryUserStorage userStorage;
    private final ValidationService validationService;
    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage,
                          FilmService filmService,
                          InMemoryUserStorage userStorage,
                          ValidationService validationService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
        this.validationService = validationService;
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /films, метод POST");
        if (!validationService.isValid(film)) {
            log.error("Ошибка валидации, недопустимые поля Film");
            throw new ValidationException();
        }

        return filmStorage.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws Exception {
        log.info("Получен запрос к эндпоинту: /films, метод PUT");
        if (filmStorage.idNotExist(film.getId())) {
            log.error("Ошибка, фильма с таким id = " + film.getId() + " не существует.");
            throw new NotFoundException(film.getId());
        }

        if (!validationService.isValid(film)) {
            log.error("Ошибка валидации, недопустимые поля Film");
            throw new ValidationException();
        }

        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id,
                        @PathVariable int userId)  throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /films/{id}/like/{userId}, метод PUT");
        if (filmStorage.idNotExist(id)) {
            log.error("Ошибка, фильма с таким id = " + id + " не существует.");
            throw new NotFoundException(id);
        }

        if (userStorage.idNotExist(userId)) {
            log.error("Ошибка, пользователя с таким id = " + userId + " не существует.");
            throw new NotFoundException(userId);
        }

        filmService.addLike(id,userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id,
                           @PathVariable int userId)  throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /films/{id}/like/{userId}, метод DELETE");
        if (filmStorage.idNotExist(id)) {
            log.error("Ошибка, фильма с таким id = " + id + " не существует.");
            throw new NotFoundException(id);
        }

        if (userStorage.idNotExist(userId)) {
            log.error("Ошибка, пользователя с таким id = " + userId + " не существует.");
            throw new NotFoundException(userId);
        }

        filmService.deleteLike(id,userId);
    }

    @GetMapping("/films")
    public List<Film> findAllFilms() {
        log.info("Получен запрос к эндпоинту: /films, метод GET");

        return filmStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /films/{id}, метод GET");
        if (filmStorage.idNotExist(id)) {
            log.error("Ошибка, фильма с таким id = " + id + " не существует.");
            throw new NotFoundException(id);
        }

        return filmStorage.findFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> findMostPopularFilms(
            @RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Получен запрос к эндпоинту: /films/popular, метод GET");

        return filmService.findMostPopularFilms(count);
    }
}
