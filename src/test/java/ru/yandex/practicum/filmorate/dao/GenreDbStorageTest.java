package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void readGenre() throws NotFoundException {
        Genre genres = genreDbStorage.readGenre(1)
                .orElseThrow(() -> new NotFoundException(1,"Ошибка, жанра с таким id = " + 1 + " не существует."));

        assertEquals("Комедия", genres.getName());
    }

    @Test
    void readAllGenre() {
        List<Genre> genres = genreDbStorage.readAllGenre();

        assertEquals(6, genres.size());
    }
}