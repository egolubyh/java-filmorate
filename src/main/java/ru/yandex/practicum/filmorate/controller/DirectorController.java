package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.ValidationService;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
public class DirectorController {

    DirectorService directorService;
    private final ValidationService validationService;

    @Autowired
    public DirectorController( DirectorService directorService,ValidationService validationService) {
        this.directorService = directorService;
        this. validationService=validationService;
    }


    @PostMapping("/directors")
    public Director createDirector(@RequestBody Director director) throws NotFoundException, ValidationException {
        if (!validationService.isValid(director)) {
            throw new ValidationException("Ошибка валидации, недопустимые поля Director");
        }


        return directorService.addDirector(director);
    }

    @PutMapping("/directors")
    public Director updateDirector(@RequestBody Director director) throws NotFoundException {
        return directorService.updateDirector(director);
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

    @DeleteMapping("/directors/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) throws SQLException {
        directorService.deleteDirectorsById(id);
        log.info("DELETE delete director");
        return ResponseEntity.ok().build();
    }
}
