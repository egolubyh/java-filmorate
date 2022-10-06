package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private ValidationService service;

    @BeforeEach
    void setUp(){
        service = new ValidationService();
    }

    @Test
    void shouldBeTrueValidUser() {
         final User user = new User(0,"mail@yandex.ru","log","name", LocalDate.now(),new HashSet<>());

         final boolean actual = service.isValid(user);

        assertTrue(actual);
    }

    @Test
    void shouldBeFalseNullUserFields() {
        final User user1 = new User(-1,"mail@yandex.ru","log","name", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,null,"log","name", LocalDate.now(),new HashSet<>());
        final User user3 = new User(0,"mail@yandex.ru",null,"name", LocalDate.now(),new HashSet<>());
        final User user4 = new User(0,"mail@yandex.ru","log","name", null,new HashSet<>());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);
        final boolean actual3 = service.isValid(user3);
        final boolean actual4 = service.isValid(user4);

        assertFalse(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }

    @Test
    void shouldBeFalseInvalidUserEmail() {
        final User user1 = new User(0,"","log","name", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,"without","log","name", LocalDate.now(),new HashSet<>());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

    @Test
    void shouldBeFalseInvalidUserLogin() {
        final User user1 = new User(0,"mail@yandex.ru","","name", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,"mail@yandex.ru"," ","name", LocalDate.now(),new HashSet<>());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

    @Test
    void shouldBeFalseInvalidUserName() {
        final User user1 = new User(0,"mail@yandex.ru","log","", LocalDate.now(),new HashSet<>());
        final User user2 = new User(0,"mail@yandex.ru","log",null, LocalDate.now(),new HashSet<>());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);

        assertTrue(actual1 && actual2);
        assertEquals("log",user1.getName());
        assertEquals("log",user2.getName());
    }

    @Test
    void shouldBeFalseInvalidUserBirthday() {
        final User user = new User(0,"mail@yandex.ru","log","name", LocalDate.now().plusDays(1),new HashSet<>());

        final boolean actual = service.isValid(user);

        assertFalse(actual);
    }

    @Test
    void shouldBeTrueValidFilm() {
        final Film film = new Film(0,"name","desc",LocalDate.now(),120,new HashSet<>());

        final boolean actual = service.isValid(film);

        assertTrue(actual);
    }

    @Test
    void shouldBeFalseNullFilmFields() {
        final Film film1 = new Film(-1,"name","desc",LocalDate.now(),120,new HashSet<>());
        final Film film2 = new Film(0,null,"desc",LocalDate.now(),120,new HashSet<>());
        final Film film3 = new Film(0,"name",null,LocalDate.now(),120,new HashSet<>());
        final Film film4 = new Film(0,"name","desc",null,120,new HashSet<>());
        final Film film5 = new Film(0,"name","desc",LocalDate.now(),null,new HashSet<>());

        final boolean actual1 = service.isValid(film1);
        final boolean actual2 = service.isValid(film2);
        final boolean actual3 = service.isValid(film3);
        final boolean actual4 = service.isValid(film4);
        final boolean actual5 = service.isValid(film5);

        assertFalse(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
        assertFalse(actual5);
    }

    @Test
    void shouldBeFalseInvalidFilmName() {
        final Film film = new Film(0,"","desc",LocalDate.now(),120,new HashSet<>());

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmDescription() {
        final Film film = new Film(0,"name",String.format("%201s","s"),LocalDate.now(),120,new HashSet<>());

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmReleaseDate() {
        final Film film = new Film(0,"name","desc",LocalDate.of(1895,12,27),120,new HashSet<>());

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmDuration() {
        final Film film = new Film(0,"name","desc",LocalDate.now(),-1,new HashSet<>());

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }
}