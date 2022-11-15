package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@RestController
public class GenreController {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    /**
     * Возвращает жанр с идентификатором переданным в параметрах метода
     * @param id идентификатор жанра
     * @return жанр
     * @throws NotFoundException если жанра с таким идентификатором
     * не существует в базе данных
     */
    @GetMapping("/genres/{id}")
    public Genre findGenre(@PathVariable long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /genres/{id}, метод GET");
        return genreDbStorage.readGenre(id)
                .orElseThrow(() -> new NotFoundException(id,"Ошибка, жанра с таким id = " + id + " не существует."));
    }

    /**
     * Возвращает список всех жанров
     * @return список жанров
     */
    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        log.info("Получен запрос к эндпоинту: /genres, метод GET");
        return genreDbStorage.readAllGenre();
    }
}
