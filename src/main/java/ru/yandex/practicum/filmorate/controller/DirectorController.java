package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
@Slf4j
@RestController
public class DirectorController {

    DirectorStorage directorStorage;

    @Autowired
    public DirectorController(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }


    @PostMapping("/directors")
    public Director createDirector(@RequestBody Director director)  {
        return directorStorage.addDirector(director);
    }



}
