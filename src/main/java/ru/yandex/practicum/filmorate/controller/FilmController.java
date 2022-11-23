package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final ValidationService validationService;
    @Autowired
    public FilmController(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                          FilmService filmService,
                          ValidationService validationService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.validationService = validationService;
    }

    /**
     * Добавить фильм.
     * @param film фильм.
     * @return Film добавленный фильм.
     * @throws ValidationException если поля фильма не допустимы.
     */
    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /films, метод POST");
        if (!validationService.isValid(film)) {
            throw new ValidationException("Ошибка валидации, недопустимые поля Film");
        }

        return filmService.createFilm(film);
    }

    /**
     * Обновить информацию о фильме.
     * @param film фильм.
     * @return Film обновленный фильм.
     * @throws NotFoundException если id переданного фильма не существует.
     * @throws ValidationException если поля фильма недопустимые.
     */
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws NotFoundException, ValidationException {
        log.info("Получен запрос к эндпоинту: /films, метод PUT");
        if (filmStorage.idNotExist(film.getId())) {
            throw new NotFoundException(film.getId(), "Ошибка, фильма с таким id = " + film.getId() + " не существует." );
        }

        if (!validationService.isValid(film)) {
            throw new ValidationException("Ошибка валидации, недопустимые поля Film");
        }

        return filmService.updateFilm(film);
    }

    /**
     * Возвращает все фильмы.
     * @return список всех пользователей.
     */
    @GetMapping("/films")
    public List<Film> findAllFilms() {
        log.info("Получен запрос к эндпоинту: /films, метод GET");

        return filmStorage.readAllFilms();
    }

    /**
     * Возвращает фильм по идентификатору.
     * @param id идентификатор фильма.
     * @return фильм.
     * @throws NotFoundException если фильма с таким идентификатором
     * не существует.
     */
    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /films/{id}, метод GET");
        if (filmStorage.idNotExist(id)) {
            throw new NotFoundException(id, "Ошибка, фильма с таким id = " + id + " не существует.");
        }

        return filmStorage.readFilm(id);
    }

    /**
     * Возвращает список из первых count фильмов по количеству лайков.
     * Если значение параметра count не задано, вернет первые 10.
     * @param allParams параметры запроса: count - максимальное кол-во возвращаемых фильмов,
     * genreId - id жанр фильма, year - год релиза фильма.
     * @return список фильмов.
     */
    @GetMapping("/films/popular")
    public List<Film> findMostPopularFilms(@RequestParam Map<String,String> allParams) {
        log.info("Получен запрос к эндпоинту: /films/popular, метод GET, RequestParam = {}",
                allParams);

        return filmService.findMostPopular(allParams);
    }

    /**
     * Удаление пользователя.
     * @param filmId идентификатор пользователя.     *
     * @throws NotFoundException если фильма с таким id не существует.
     */

    @DeleteMapping("/films/{filmId}")
    public void deleteFilm(@PathVariable long filmId) throws NotFoundException {
        log.info("Запрошено удаление фильма с id " + filmId);
        Film film = filmStorage.readFilm(filmId);
        if (film != null) {
            log.info("Удаляем фильм " + film.getName());
            filmStorage.deleteFilm(filmId);
        } else {
            throw new NotFoundException(filmId, "Фильм с id" + filmId + " не найден.");
        }
    }

    @GetMapping("/films/director/{directorId}")
    public Collection<Film> getFilmsByDirector(
            @PathVariable Long directorId,
            @RequestParam(required = false, defaultValue = "year") String sortBy) throws SQLException, NotFoundException {
        log.info("getFilmsByDirector");
        if (filmStorage.idDirectorNotExist(directorId)) {
            throw new NotFoundException(directorId, "Ошибка, фильма с таким id = " + directorId + " не существует.");
        }
        return filmService.findFilmsByDirectorsId(directorId, sortBy);
    }


    @GetMapping("/films/{id}/friends/common/{otherId}")
    public List<Film> findCommonFilms(@PathVariable Long id, @PathVariable Long otherId) {
        return filmService.GetCommonFilms(id,otherId);
    }
}
