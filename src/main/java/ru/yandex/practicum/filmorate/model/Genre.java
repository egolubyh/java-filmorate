package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Genre {
    /**
     * Идентификатор genre
     */
    private long id;
    /**
     * Наименование genre
     */
    private String name;
}
