package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public User findUserById(int id) {
        return users.get(id);
    }

    public List<User> findAllFriendsUserById(int id) {
        return users.get(id).getFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public boolean idNotExist(int id) {
        return !users.containsKey(id);
    }


}
