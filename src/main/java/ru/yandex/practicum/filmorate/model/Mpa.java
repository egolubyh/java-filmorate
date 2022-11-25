package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Mpa {
    /**
     * Идентификатор mpa
     */
    private long id;
    /**
     * Наименование mpa
     */
    private String name;
}
