package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final InMemoryUserStorage userStorage;
    private final UserService userService;
    private final ValidationService validationService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage,
                          UserService userService,
                          ValidationService validationService) {
        this.userStorage = userStorage;
        this.userService = userService;
        this.validationService = validationService;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /users, метод POST");
        if (!validationService.isValid(user)) {
            log.error("Ошибка валидации, недопустимые поля User");
            throw new ValidationException();
        }

        return userStorage.createUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws Exception {
        log.info("Получен запрос к эндпоинту: /users, метод PUT");
        if (userStorage.idNotExist(user.getId())) {
            log.error("Ошибка, пользователя с таким id = " + user.getId() + " не существует.");
            throw new NotFoundException(user.getId());
        }

        if (!validationService.isValid(user)) {
            log.error("Ошибка валидации, недопустимые поля User");
            throw new ValidationException();
        }

        return userStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id,
                           @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends/{friendId}, метод PUT");
        if (userStorage.idNotExist(id)) {
            log.error("Ошибка, пользователя с таким id = " + id + " не существует.");
            throw new NotFoundException(id);
        }
        if (userStorage.idNotExist(friendId)) {
            log.error("Ошибка, пользователя с таким id = " + friendId + " не существует.");
            throw new NotFoundException(friendId);
        }

        userService.addFriend(id,friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id,
                              @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends/{friendId}, метод DELETE");
        if (userStorage.idNotExist(id)) {
            log.error("Ошибка, пользователя с таким id = " + id + " не существует.");
            throw new NotFoundException(id);
        }
        if (userStorage.idNotExist(friendId)) {
            log.error("Ошибка, пользователя с таким id = " + friendId + " не существует.");
            throw new NotFoundException(friendId);
        }

        userService.deleteFriend(id,friendId);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        log.info("Получен запрос к эндпоинту: /users, метод GET");

        return userStorage.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable int id) throws Exception {
        log.info("Получен запрос к эндпоинту: /users/{id}, метод GET");
        if (userStorage.idNotExist(id)) {
            log.error("Ошибка, пользователя с таким id = " + id + " не существует.");
            throw new NotFoundException(id);
        }
        return userStorage.findUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriendsUserById(@PathVariable int id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends, метод GET");
        if (userStorage.idNotExist(id)) {
            log.error("Ошибка, пользователя с таким id = " + id + "не существует.");
            throw new NotFoundException(id);
        }

        return userStorage.findAllFriendsUserById(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findAllMutualFriends(@PathVariable int id,
                           @PathVariable int otherId) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends/common/{otherId}, метод GET");
        if (userStorage.idNotExist(id)) {
            log.error("Ошибка, пользователя с таким id = " + id + "не существует.");
            throw new NotFoundException(id);
        }

        if (userStorage.idNotExist(otherId)) {
            log.error("Ошибка, пользователя с таким id = " + otherId + "не существует.");
            throw new NotFoundException(otherId);
        }

        return userService.findAllMutualFriends(id,otherId);
    }
}
