package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FriendsDbStorage friendsDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       FriendsDbStorage friendsDbStorage,
                       JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.friendsDbStorage = friendsDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Пользователь добавляет друга.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор друга.
     */
    public void addFriend(long userId, long friendId) {
        friendsDbStorage.createFriendship(userId,friendId);

    }

    /**
     * Пользователь удаляет друга.
     * @param userId идентификатор пользователя.
     * @param friendId идентификатор друга.
     */
    public void deleteFriend(int userId, int friendId) {
        friendsDbStorage.deleteFriendship(userId,friendId);
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

    public List<Film> findRecommendedFilms(long id) {
        List<Film> films = filmStorage.findRecommendedFilms(id);
        /*log.debug("Рекомендовано для пользователя #{} {} фильмов", userId, films.size());*/
        return films;
    }
}
