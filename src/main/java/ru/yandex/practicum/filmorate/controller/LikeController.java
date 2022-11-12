package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@RestController
public class LikeController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public LikeController(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                          @Qualifier("userDbStorage") UserStorage userStorage,
                          FilmService filmService) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /**
     * Пользователь ставит лайк фильму.
     * @param id идентификатор фильма.
     * @param userId идентификатор пользователя.
     * @throws NotFoundException если пользователя или фильма с таким идентификатором
     * не существует.
     */
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id,
                        @PathVariable long userId)  throws NotFoundException {
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

    /**
     * Пользователь удаляет лайк.
     * @param id идентификатор фильма.
     * @param userId идентификатор пользователя.
     * @throws NotFoundException если пользователя или фильма с таким идентификатором
     * не существует.
     */

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id,
                           @PathVariable long userId)  throws NotFoundException {
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

}
