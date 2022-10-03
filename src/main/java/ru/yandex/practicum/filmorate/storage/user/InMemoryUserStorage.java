package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User deleteUser(User user) {
        users.remove(user.getId());

        return user;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public boolean userIsExist(int id) {
        return users.containsKey(id);
    }
}
