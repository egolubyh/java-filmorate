package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

   @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(final Exception e) {
        return "Произошла непредвиденная ошибка.";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(final NotFoundException e) {
        return "Произошла ошибка, переданный id = " + e.getId() + " не существует.";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(final ValidationException e) {
        return "Произошла ошибка валидации" ;
    }
}
