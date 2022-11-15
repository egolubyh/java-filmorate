package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreDbStorageTest {

    private final FilmGenreDbStorage filmGenreDbStorage;

    @Test
    void createFilmGenre() {
        filmGenreDbStorage.createFilmGenre(9, List.of(1L,2L,3L,4L,5L));

        assertEquals(5, filmGenreDbStorage.readAllFilmGenre(9).size());
        assertEquals(3, filmGenreDbStorage.readAllFilmGenre(9).get(2).getId());
    }

    @Test
    void readAllFilmGenre() {
        final List<Long> idList = filmGenreDbStorage.readAllFilmGenre(7)
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        assertEquals(3, idList.size());
        assertTrue(idList.containsAll(List.of(6L,1L,2L)));
    }

    @Test
    void deleteFilmGenre() {
        filmGenreDbStorage.deleteFilmGenre(7);
        final List<Long> idList = filmGenreDbStorage.readAllFilmGenre(7)
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toList());

        assertEquals(0, idList.size());
    }
}