package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class ValidationService {

    /**
     * Проверка всех полей фильма на допустимость.
     *
     * @param film фильм.
     * @return результат соответствия true или false.
     */
    public boolean isValid(Film film) {
        if (film.getId() < 0) return false;
        if (film.getName() == null || film.getDescription() == null
                || film.getReleaseDate() == null) return false;
        if (film.getName().isEmpty()) return false;
        if (film.getDescription().length() > 200) return false;
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) return false;
        return film.getDuration() > 0;
    }    

    /**
     * Проверка всех полей пользователя на допустимость.
     *
     * @param user пользователь.
     * @return результат соответствия true или false.
     */
    public boolean isValid(User user) {
        if (user.getId() < 0) return false;
        if (user.getEmail() == null || user.getLogin() == null || user.getBirthday() == null) return false;
        if (user.getName() == null || user.getName().isEmpty()) user.setName(user.getLogin());
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) return false;
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) return false;
        return !user.getBirthday().isAfter(LocalDate.now());
    }

    public boolean isValid(Director director) {
        return !director.getName().isBlank();
    }
}
