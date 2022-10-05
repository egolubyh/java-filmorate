package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

@Data
public class NotFoundException extends Exception {
    private final int id;
}
