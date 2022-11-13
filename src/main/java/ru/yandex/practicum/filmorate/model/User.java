package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    /**
     * Идентификатор пользователя
     */
    private long id;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Логин пользователя
     */
    private String login;
    /**
     * Адрес электронной почты пользователя
     */
    private String email;
    /**
     * Дата рождения пользователя
     */
    private LocalDate birthday;
}
