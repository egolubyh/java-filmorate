package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void createFilm() {
        final Film film = new Film();
        film.setName("up");
        film.setDescription("up");
        film.setReleaseDate(LocalDate.of(1999,1,1));
        film.setDuration(10);
        film.setRate(10);
        Mpa mpa = new Mpa();
        mpa.setId(2);
        film.setMpa(mpa);

        long id = filmDbStorage.createFilm(film).getId();

        Film film1 = filmDbStorage.readFilm(13);
        assertEquals(id,film1.getId());
        assertEquals(film.getName(),film1.getName());
        assertEquals(film.getDescription(),film1.getDescription());
        assertEquals(film.getReleaseDate(),film1.getReleaseDate());
        assertEquals(film.getDuration(),film1.getDuration());
        assertEquals(film.getRate(),film1.getRate());
        assertEquals(film.getMpa().getId(),film1.getMpa().getId());
    }

    @Test
    void readFilm() {
        final Film film = filmDbStorage.readFilm(1);

        assertEquals(1, film.getId());
        assertEquals("Король Лев", film.getName());
        assertEquals("desc" , film.getDescription());
        assertEquals(LocalDate.of(1994,10,10) , film.getReleaseDate());
        assertEquals(88 , film.getDuration());
        assertEquals(4 , film.getRate());
        assertEquals(1 , film.getMpa().getId());
    }

    @Test
    void readAllFilms() {
        final List<Film> films = filmDbStorage.readAllFilms();
        final Film film = films.get(11);

        assertEquals(12, films.size());

        assertEquals(12, film.getId());
        assertEquals("Трудная мишень", film.getName());
        assertEquals("desc" , film.getDescription());
        assertEquals(LocalDate.of(1993,10,10) , film.getReleaseDate());
        assertEquals(122 , film.getDuration());
        assertEquals(4 , film.getRate());
        assertEquals(5 , film.getMpa().getId());

    }

    @Test
    void updateFilm() {
        final Film upFilm = new Film();
        upFilm.setId(1);
        upFilm.setName("up");
        upFilm.setDescription("up");
        upFilm.setReleaseDate(LocalDate.of(1999,1,1));
        upFilm.setDuration(10);
        upFilm.setRate(10);
        Mpa mpa = new Mpa();
        mpa.setId(2);
        upFilm.setMpa(mpa);

        filmDbStorage.updateFilm(upFilm);
        final Film actual = filmDbStorage.readFilm(1);

        assertEquals(1, actual.getId());
        assertEquals("up", actual.getName());
        assertEquals("up", actual.getDescription());
        assertEquals(LocalDate.of(1999,1,1), actual.getReleaseDate());
        assertEquals(10, actual.getDuration());
        assertEquals(10, actual.getRate());
        assertEquals(2, actual.getMpa().getId());
    }

    @Test
    void deleteFilm() {
        filmDbStorage.deleteFilm(13);

        assertEquals(12, filmDbStorage.readAllFilms().size());
    }

    @Test
    void idNotExist() {
        assertTrue(filmDbStorage.idNotExist(999));
    }
}
