package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@RestController
public class MpaController {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaController(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    /**
     * Возвращает список имеющихся рейтингов фильма
     * @return список рейтингов фильмов
     */
    @GetMapping("/mpa")
    public List<Mpa> findAllMpa() {
        log.info("Получен запрос к эндпоинту: /mpa, метод GET");
        return mpaDbStorage.readAllMpa();
    }

    /**
     * Возвращает рейтинг фильма с переданным идентификатором
     * @param id идентификатор рейтинга
     * @return рейтинг фильма
     * @throws NotFoundException если рейтинга с переданным идентификатором
     * нет в базе данных
     */
    @GetMapping("/mpa/{id}")
    public Mpa findGenre(@PathVariable long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /mpa/{id}, метод GET");
        if (mpaDbStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, рейтинга с таким id = " + id + " не существует.");
        }
        return mpaDbStorage.readMpa(id);
    }
}
