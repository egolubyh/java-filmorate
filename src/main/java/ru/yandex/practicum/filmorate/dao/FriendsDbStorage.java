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

    public void createFriendship(long userId, long friendId) {
        String sqlQuery = "INSERT INTO FRIENDS (FRIEND_ONE, FRIEND_TWO, CONFIRMED) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId, false);
    }

    public void deleteFriendship(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE ID = (SELECT ID FROM FRIENDS " +
                "WHERE FRIEND_ONE = ? AND FRIEND_TWO = ?)";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}
