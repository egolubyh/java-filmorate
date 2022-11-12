package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Пользователь добавляет друга.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор друга.
     */
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "INSERT INTO FRIENDS (FRIEND_ONE, FRIEND_TWO, CONFIRMED) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId, false);
    }

    /**
     * Пользователь удаляет друга.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор друга.
     */
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE ID = (SELECT ID FROM FRIENDS " +
                "WHERE FRIEND_ONE = " + userId + " AND FRIEND_TWO = " + friendId + ")";

        jdbcTemplate.update(sqlQuery);
    }

    /**
     * Получить список общих друзей.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор друга.
     * @return список общих друзей.
     */
    public List<User> findAllMutualFriends(long userId, long friendId) {
        Set<Long> user = userStorage.readAllFriends(userId)
                .stream()
                .map(User::getId).collect(Collectors.toSet());
        Set<Long> friends = userStorage.readAllFriends(friendId)
                .stream()
                .map(User::getId).collect(Collectors.toSet());

        friends.retainAll(user);
        return friends.stream()
                .map(userStorage::readUser)
                .collect(Collectors.toList());
    }
}
