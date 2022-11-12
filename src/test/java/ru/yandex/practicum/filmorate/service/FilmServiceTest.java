package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
   /* private InMemoryFilmStorage storage;
    private FilmService service;

    @BeforeEach
    void setUp() {
        storage = new InMemoryFilmStorage();
        service = new FilmService(storage);

        storage.createFilm(new Film(0,"a","b", LocalDate.now(),120,new HashSet<>())); //id=1
        storage.createFilm(new Film(0,"a","b", LocalDate.now(),120,
                new HashSet<>(Arrays.asList(1,2,3,4,5)))); //id=2
        storage.createFilm(new Film(0,"a","b", LocalDate.now(),120,
                new HashSet<>(Arrays.asList(1,2,3,4)))); //id=3
        storage.createFilm(new Film(0,"a","b", LocalDate.now(),120,
                new HashSet<>(Arrays.asList(1,2,3,6,7,8,4)))); //id=4
    }

    @Test
    void addLike() {
        service.addLike(1,1);

        final Set<Integer> likes = storage.findFilmById(1).getLikes();

        assertEquals(1,likes.size());
        assertEquals(Set.of(1),likes);
    }

    @Test
    void deleteLike() {
        service.deleteLike(2,3);

        final Set<Integer> likes = storage.findFilmById(2).getLikes();

        assertEquals(4,likes.size());
        assertEquals(Set.of(1,2,4,5),likes);

    }

    @Test
    void findMostPopularFilms() {
        final List<Film> films = service.findMostPopularFilms(3);
        final List<Integer> list = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        assertEquals(List.of(4,2,3),list);
    }*/
}