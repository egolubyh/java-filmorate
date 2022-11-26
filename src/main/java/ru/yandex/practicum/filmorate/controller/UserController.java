package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;
    private final ValidationService validationService;

    private final ActivityService activityService;

    @Autowired
    public UserController(@Qualifier("userDbStorage") UserStorage userStorage,
                          UserService userService,
                          ValidationService validationService,
                          ActivityService activityService) {
        this.userStorage = userStorage;
        this.userService = userService;
        this.validationService = validationService;
        this.activityService = activityService;
    }

    /**
     * Добавить нового пользователя.
     * @param user пользователь.
     * @return Возвращает добавленного пользователя с присвоенным идентификатором.
     * @throws ValidationException если поля пользователя недопустимы.
     */
    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /users, метод POST");
        if (!validationService.isValid(user)) {
            throw new ValidationException("Ошибка валидации, недопустимые поля User");
        }
        return userStorage.createUser(user);
    }

    /**
     * Обновить информацию о пользователе.
     * @param user пользователь.
     * @return Возвращает обновленного пользователя.
     * @throws Exception если пользователя с таким id не существует или недопустимые поля пользователя.
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws Exception {
        log.info("Получен запрос к эндпоинту: /users, метод PUT");
        if (userStorage.idNotExist(user.getId())) {
            throw new NotFoundException(user.getId(),"Ошибка, пользователя с таким id = " + user.getId() + " не существует.");
        }

        if (!validationService.isValid(user)) {
            throw new ValidationException("Ошибка валидации, недопустимые поля User");
        }

        return userStorage.updateUser(user);
    }

    /**
     * Удаление пользователя.
     * @param userId идентификатор пользователя.     *
     * @throws NotFoundException если пользователя с таким id не существует.
     */
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) throws NotFoundException {
        log.info("Запрошено удаление пользователя с id " + userId);
        User user = userStorage.readUser(userId);
        if (user != null) {
            log.info("Удаляем пользователя с id " + user.getId());
            userStorage.deleteUser(userId);
        } else {
            throw new NotFoundException(userId, "Пользователь с id" + userId + " не найден.");
        }
    }


    /**
     * Получить список всех пользователей.
     * @return список всех пользователей.
     */
    @GetMapping("/users")
    public List<User> findAllUsers() {
        log.info("Получен запрос к эндпоинту: /users, метод GET");

        return userStorage.readAllUsers();
    }

    /**
     * Получить пользователя по id.
     * @param id идентификатор пользователя.
     * @return User пользователь.
     * @throws NotFoundException если пользователя с таким id не существует.
     */
    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}, метод GET");
        if (userStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, пользователя с таким id = " + id + " не существует.");
        }
        return userStorage.readUser(id);
    }

    /**
     * Получить список всех друзей конкретного пользователя.
     * @param id идентификатор пользователя.
     * @return список друзей.
     * @throws NotFoundException если пользователя с таким id не существует.
     */
    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriendsUserById(@PathVariable long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends, метод GET");
        if (userStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, пользователя с таким id = " + id + "не существует.");
        }

        return userStorage.readAllFriends(id);
    }

    /**
     * Получить список общих друзей пользователя с другом.
     * @param id идентификатор пользователя.
     * @param otherId идентификатор друга.
     * @return Список общих друзей.
     * @throws NotFoundException если пользователя или друга с таким id не существует.
     */
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findAllMutualFriends(@PathVariable long id,
                           @PathVariable long otherId) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends/common/{otherId}, метод GET");
        if (userStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, пользователя с таким id = " + id + "не существует.");
        }

        if (userStorage.idNotExist(otherId)) {
            throw new NotFoundException(otherId,"Ошибка, пользователя с таким id = " + otherId + "не существует.");
        }

        return userService.findAllMutualFriends(id,otherId);
    }

    /**
     * Получить список рекомендованных фильмов для конкретного пользователя.
     * @param id идентификатор пользователя.
     * @return список фильмов.
     * @throws NotFoundException если пользователя с таким id не существует.
     */
    @GetMapping("users/{id}/recommendations")
    public List<Film> findRecommendedFilms(@PathVariable(value = "id") long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/recommendations, метод GET");
        if (userStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, пользователя с таким id = " + id + "не существует.");
        }
        return userService.findRecommendedFilms(id);
        }
        
        
    /**
     * Получить ленту событий для указанного userId.
     * @param id идентификатор пользователя.
     * @return Лента событий.
     */
    @GetMapping("/users/{id}/feed")
    public List<UserActivity> getActivitiesByUserId(@PathVariable long id) {
        log.info("Получен запрос к эндпоинту: /users/{id}/feed, метод GET");
        return activityService.getAllByUserId(id);
    }
}
