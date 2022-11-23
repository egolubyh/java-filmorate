package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    /**
     * Идентификатор фильма
     */
    private long id;
    /**
     * Название фильма
     */
    private String name;
    /**
     * Описание фильма
     */
    private String description;
    /**
     * Дата выхода фильма в прокат
     */
    private LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах
     */
    private int duration;
    /**
     * Оценка фильма
     */
    private int rate;
    /**
     * Рейтинг фильма
     */
    private Mpa mpa;
    /**
     * Список жанров фильма
     */
    private List<Genre> genres;
    private List<Director> directors;

}
