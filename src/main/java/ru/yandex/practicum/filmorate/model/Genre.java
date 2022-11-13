package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    /**
     * Идентификатор жанра
     */
    private long id;
    /**
     * Название жанра
     */
    private String name;
}
