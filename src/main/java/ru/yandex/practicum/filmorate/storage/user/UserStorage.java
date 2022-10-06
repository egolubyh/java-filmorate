package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    User deleteUser(User user);
}
