package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
public class MpaController {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaController(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }


    @GetMapping("/mpa")
    public List<Mpa> findAllMpa() {
        return mpaDbStorage.readAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findGenre(@PathVariable long id) throws NotFoundException {
        if (mpaDbStorage.idNotExist(id)) {
            throw new NotFoundException(id);
        }
        return mpaDbStorage.readMpa(id);
    }
}
