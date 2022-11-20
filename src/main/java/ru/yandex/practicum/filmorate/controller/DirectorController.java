package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
public class DirectorController {

    DirectorService directorService;

    @Autowired
    public DirectorController( DirectorService directorService) {
        this.directorService = directorService;
    }


    @PostMapping("/directors")
    public Director createDirector(@RequestBody Director director) throws NotFoundException {
        return directorService.addDirector(director);
    }

    @GetMapping("/directors/{id}")
    public Director findDirectorById(@PathVariable long id) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /directors/{id}, метод GET");
        return directorService.findDirectorById(id);
    }

    @GetMapping("/directors")
    public List<Director> findAllFilms() {
        log.info("Получен запрос к эндпоинту: /directors, метод GET");

        return directorService.findAllDirectors();
    }



}
