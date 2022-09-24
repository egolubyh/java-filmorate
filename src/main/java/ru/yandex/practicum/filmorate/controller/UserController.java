package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private int id;

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {
        if (!valid(user)) throw new ValidationException();
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId()) || !valid(user)) throw new ValidationException();
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    private boolean valid(User user) {
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) return false;
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) return false;
        return !user.getBirthday().isAfter(LocalDate.now());
    }
}
