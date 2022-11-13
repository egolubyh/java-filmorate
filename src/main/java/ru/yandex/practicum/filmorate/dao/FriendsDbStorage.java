package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавить запись о дружбе в базу данных
     * @param userId идентификатор пользователя, который добавляет друга
     * @param friendId идентификатор друга, которого добавляет пользователь
     */
    public void createFriendship(long userId, long friendId) {
        String sqlQuery = "INSERT INTO FRIENDS (FRIEND_ONE, FRIEND_TWO, CONFIRMED) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId, false);
    }

    /**
     * Удалить запись о дружбе в базу данных
     * @param userId идентификатор пользователя, который добавляет друга
     * @param friendId идентификатор друга, которого добавляет пользователь
     */
    public void deleteFriendship(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE ID = (SELECT ID FROM FRIENDS " +
                "WHERE FRIEND_ONE = ? AND FRIEND_TWO = ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
