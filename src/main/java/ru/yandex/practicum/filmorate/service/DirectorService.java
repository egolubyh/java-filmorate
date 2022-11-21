package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
@Service
public class DirectorService {


    DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director addDirector(Director director) throws NotFoundException {
        return directorStorage.addDirector(director);
    }

    public Director findDirectorById(Long id) throws NotFoundException {
        return directorStorage.findDirectorById(id);
    }


    public List<Director> findAllDirectors() {
        return directorStorage.findAllDirectors();
    }

    public Director updateDirector(Director director) throws NotFoundException {
        return directorStorage.updateDirector(director);
    }


    public void deleteDirectorsById(long id) {
        directorStorage.deleteDirectorsById(id);
    }



}
