package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filmorate")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping(value = "/user")
    public User createUser(@RequestBody User user) {
        return users.put(user.getId(), user);
    }

    @PutMapping(value = "user")
    public User updateUser(@RequestBody User user) {
        return users.put(user.getId(), user);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}
