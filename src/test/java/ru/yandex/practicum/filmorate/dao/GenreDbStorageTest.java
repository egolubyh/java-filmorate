package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void createFilmGenre() {
    }

    @Test
    void readGenre() {
        Genre genres = genreDbStorage.readGenre(1);

        assertEquals("Комедия", genres.getName());
    }

    @Test
    void readAllGenre() {
        List<Genre> genres = genreDbStorage.readAllGenre();

        assertEquals(6, genres.size());
    }

    @Test
    void testReadAllGenre() {
        List<Genre> genres = genreDbStorage.readAllGenre(1);

        assertEquals(1, genres.size());
        assertEquals("Мультфильм", genres.get(0).getName());
    }

    @Test
    void deleteFilmGenre() {
        genreDbStorage.deleteFilmGenre(7);

        assertTrue(genreDbStorage.idNotExist(7));
    }

    @Test
    void idNotExist() {
        assertTrue(genreDbStorage.idNotExist(999));
    }
}