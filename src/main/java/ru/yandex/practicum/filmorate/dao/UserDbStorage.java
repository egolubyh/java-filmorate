package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавить нового пользователя в базу данных
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4,  Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    /**
     * Получить пользователя
     * @param id идентификатор пользователя
     * @return пользователь
     */
    @Override
    public User readUser(long id) {
        String sqlQuery = "SELECT ID, NAME, LOGIN, EMAIL, BIRTHDAY " +
                " FROM USERS WHERE ID = " + id;

        return jdbcTemplate.queryForObject(sqlQuery, new BeanPropertyRowMapper<>(User.class));
    }

    /**
     * Получить список всех пользователей
     * @return список пользователей
     */
    @Override
    public List<User> readAllUsers() {
        String sqlQuery = "SELECT ID, NAME, LOGIN, EMAIL, BIRTHDAY " +
                " FROM USERS";

        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class));
    }

    /**
     * Получить список всех друзей пользователя
     * @param userId идентификатор пользователя
     * @return список друзей
     */
    @Override
    public List<User> readAllFriends(long userId) {
        String sqlQuery = "SELECT U.ID, U.NAME, U.LOGIN, U.EMAIL, U.BIRTHDAY " +
                "FROM USERS AS U " +
                "JOIN FRIENDS AS F ON U.ID = F.FRIEND_TWO " +
                "WHERE F.FRIEND_ONE = " + userId;

        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(User.class));
    }

    /**
     * Обновить информацию о пользователе
     * @param user пользователь с обновленной информацией
     * @return пользователь
     */
    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE USERS SET " +
                "NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ?" +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getLogin()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    /**
     * Удаление пользователя из базы данных
     * @param id пользователя
     */
    @Override
    public void deleteUser(long id) {
        String sqlQuery = "DELETE FROM USERS WHERE ID = ?";

        jdbcTemplate.update(sqlQuery, id);
    }

    /**
     * Проверяет существование идентификатора
     * @param id идентификатор пользователя
     * @return результат условия
     */
    @Override
    public boolean idNotExist(long id) {
        String sqlQuery = "SELECT EXISTS(SELECT * FROM USERS WHERE ID = ?)";

        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id));
    }
}
