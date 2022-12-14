package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private int id;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public List<User> readAllFriends(long userId) {
        return null;
    }

    @Override
    public User readUser(long id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean idNotExist(long id) {
        return !users.containsKey(id);
    }


}
