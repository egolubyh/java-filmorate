package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    @Test
    void shouldBeTrueValidUser() {
         final User user = new User(0,"mail@yandex.ru","log","name", LocalDate.now(),new HashSet<>());

         final boolean actual = ValidationService.valid(user);

        assertTrue(actual);
    }

    @Test
    void shouldBeFalseNullUserFields() {
        final User user1 = new User(-1,"mail@yandex.ru","log","name", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,null,"log","name", LocalDate.now(),new HashSet<>());
        final User user3 = new User(0,"mail@yandex.ru",null,"name", LocalDate.now(),new HashSet<>());
        final User user4 = new User(0,"mail@yandex.ru","log","name", null,new HashSet<>());

        final boolean actual1 = ValidationService.valid(user1);
        final boolean actual2 = ValidationService.valid(user2);
        final boolean actual3 = ValidationService.valid(user3);
        final boolean actual4 = ValidationService.valid(user4);

        assertFalse(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }

    @Test
    void shouldBeFalseInvalidUserEmail() {
        final User user1 = new User(0,"","log","name", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,"without","log","name", LocalDate.now(),new HashSet<>());

        final boolean actual1 = ValidationService.valid(user1);
        final boolean actual2 = ValidationService.valid(user2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

    @Test
    void shouldBeFalseInvalidUserLogin() {
        final User user1 = new User(0,"mail@yandex.ru","","name", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,"mail@yandex.ru"," ","name", LocalDate.now(),new HashSet<>());

        final boolean actual1 = ValidationService.valid(user1);
        final boolean actual2 = ValidationService.valid(user2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

    @Test
    void shouldBeFalseInvalidUserName() {
        final User user1 = new User(0,"mail@yandex.ru","log","", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,"mail@yandex.ru","log",null, LocalDate.now(),new HashSet<>());

        final boolean actual1 = ValidationService.valid(user1);
        final boolean actual2 = ValidationService.valid(user2);

        assertTrue(actual1 && actual2);
        assertEquals("log",user1.getName());
        assertEquals("log",user2.getName());
    }

    @Test
    void shouldBeFalseInvalidUserBirthday() {
        final User user = new User(0,"mail@yandex.ru","log","name", LocalDate.now().plusDays(1),new HashSet<>());

        final boolean actual = ValidationService.valid(user);

        assertFalse(actual);
    }

    @Test
    void shouldBeTrueValidFilm() {
        final Film film = new Film(0,"name","desc",LocalDate.now(),120,new HashSet<>());

        final boolean actual = ValidationService.valid(film);

        assertTrue(actual);
    }

    @Test
    void shouldBeFalseNullFilmFields() {
        final Film film1 = new Film(-1,"name","desc",LocalDate.now(),120,new HashSet<>());
        final Film film2 = new Film(0,null,"desc",LocalDate.now(),120,new HashSet<>());
        final Film film3 = new Film(0,"name",null,LocalDate.now(),120,new HashSet<>());
        final Film film4 = new Film(0,"name","desc",null,120,new HashSet<>());
        final Film film5 = new Film(0,"name","desc",LocalDate.now(),null,new HashSet<>());

        final boolean actual1 = ValidationService.valid(film1);
        final boolean actual2 = ValidationService.valid(film2);
        final boolean actual3 = ValidationService.valid(film3);
        final boolean actual4 = ValidationService.valid(film4);
        final boolean actual5 = ValidationService.valid(film5);

        assertFalse(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
    }

    @Test
    void shouldBeFalseInvalidFilmName() {
        final Film film = new Film(0,"","desc",LocalDate.now(),120,new HashSet<>());

        final boolean actual = ValidationService.valid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmDescription() {
        final Film film = new Film(0,"name",String.format("%201s","s"),LocalDate.now(),120,new HashSet<>());

        final boolean actual = ValidationService.valid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmReleaseDate() {
        final Film film = new Film(0,"name","desc",LocalDate.of(1895,12,27),120,new HashSet<>());

        final boolean actual = ValidationService.valid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmDuration() {
        final Film film = new Film(0,"name","desc",LocalDate.now(),-1,new HashSet<>());

        final boolean actual = ValidationService.valid(film);

        assertFalse(actual);
    }
}