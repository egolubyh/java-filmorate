package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    /**
     * Идентификатор рейтинга
     */
    private long id;
    /**
     * Название рейтинга
     */
    private String name;
}
