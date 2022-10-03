package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /users, метод POST");
        if (!ValidationService.valid(user)) {
            log.debug("Ошибка");
            throw new ValidationException();
        }

        return userStorage.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос к эндпоинту: /users, метод PUT");
        if (!userStorage.userIsExist(user.getId()) || !ValidationService.valid(user)) {
            log.debug("Ошибка");
            throw new ValidationException();
        }

        return userStorage.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        log.info("Получен запрос к эндпоинту: /users, метод GET");
        return userStorage.findAll();
    }
}
