package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
public class GenreController {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        return genreDbStorage.readAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenre(@PathVariable long id) throws NotFoundException {
        if (genreDbStorage.idNotExist(id)) {
            throw new NotFoundException(id);
        }
        return genreDbStorage.readGenre(id);
    }
}
