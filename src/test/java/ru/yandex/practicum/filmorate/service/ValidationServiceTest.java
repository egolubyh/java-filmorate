package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private ValidationService service;

    @BeforeEach
    void setUp(){
        service = new ValidationService();
    }

    @Test
    void shouldBeTrueValidUser() {
        final User user = new User();
        user.setId(0);
        user.setEmail("mail@yandex.ru");
        user.setName("name");
        user.setLogin("log");
        user.setBirthday(LocalDate.now());

        final boolean actual = service.isValid(user);

        assertTrue(actual);
    }

    @Test
    void shouldBeFalseNullUserFields() {
        final User user1 = new User();
        user1.setId(-1);
        user1.setEmail("mail@yandex.ru");
        user1.setName("name");
        user1.setLogin("log");
        user1.setBirthday(LocalDate.now());

        final User user2 = new User();
        user2.setId(0);
        user2.setEmail(null);
        user2.setName("name");
        user2.setLogin("log");
        user2.setBirthday(LocalDate.now());

        final User user3 = new User();
        user3.setId(0);
        user3.setEmail("mail@yandex.ru");
        user3.setName("name");
        user3.setLogin(null);
        user3.setBirthday(LocalDate.now());

        final User user4 = new User();
        user4.setId(0);
        user4.setEmail("mail@yandex.ru");
        user4.setName("name");
        user4.setLogin("log");
        user4.setBirthday(null);

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
        final User user1 = new User();
        user1.setId(0);
        user1.setEmail("");
        user1.setName("name");
        user1.setLogin("log");
        user1.setBirthday(LocalDate.now());

        final User user2 = new User();
        user2.setId(0);
        user2.setEmail("without");
        user2.setName("name");
        user2.setLogin("");
        user2.setBirthday(LocalDate.now());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

    @Test
    void shouldBeFalseInvalidUserLogin() {
        final User user1 = new User();
        user1.setId(0);
        user1.setEmail("mail@yandex.ru");
        user1.setName("name");
        user1.setLogin("");
        user1.setBirthday(LocalDate.now());

        final User user2 = new User();
        user2.setId(0);
        user2.setEmail("mail@yandex.ru");
        user2.setName("name");
        user2.setLogin(" ");
        user2.setBirthday(LocalDate.now());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);

        assertFalse(actual1);
        assertFalse(actual2);
    }

    @Test
    void shouldBeFalseInvalidUserName() {
        final User user1 = new User();
        user1.setId(0);
        user1.setEmail("mail@yandex.ru");
        user1.setName("");
        user1.setLogin("log");
        user1.setBirthday(LocalDate.now());

        final User user2 = new User();
        user2.setId(0);
        user2.setEmail("mail@yandex.ru");
        user2.setName(null);
        user2.setLogin("log");
        user2.setBirthday(LocalDate.now());

        final boolean actual1 = service.isValid(user1);
        final boolean actual2 = service.isValid(user2);

        assertTrue(actual1 && actual2);
        assertEquals("log",user1.getName());
        assertEquals("log",user2.getName());
    }

    @Test
    void shouldBeFalseInvalidUserBirthday() {
        final User user = new User();
        user.setId(0);
        user.setEmail("mail@yandex.ru");
        user.setName("name");
        user.setLogin("log");
        user.setBirthday(LocalDate.now().plusDays(1));

        final boolean actual = service.isValid(user);

        assertFalse(actual);
    }

    @Test
    void shouldBeTrueValidFilm() {
        final Film film = new Film();
        film.setId(0);
        film.setName("name");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        final boolean actual = service.isValid(film);

        assertTrue(actual);
    }

    @Test
    void shouldBeFalseNullFilmFields() {
        final Film film1 = new Film();
        film1.setId(-1);
        film1.setName("name");
        film1.setDescription("desc");
        film1.setReleaseDate(LocalDate.now());
        film1.setDuration(120);
        final Film film2 = new Film();
        film2.setId(0);
        film2.setName(null);
        film2.setDescription("desc");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(120);
        final Film film3 = new Film();
        film3.setId(0);
        film3.setName("name");
        film3.setDescription(null);
        film3.setReleaseDate(LocalDate.now());
        film3.setDuration(120);
        final Film film4 = new Film();
        film4.setId(0);
        film4.setName("name");
        film4.setDescription("desc");
        film4.setReleaseDate(null);
        film4.setDuration(120);

        final boolean actual1 = service.isValid(film1);
        final boolean actual2 = service.isValid(film2);
        final boolean actual3 = service.isValid(film3);
        final boolean actual4 = service.isValid(film4);

        assertFalse(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }

    @Test
    void shouldBeFalseInvalidFilmName() {
        final Film film = new Film();
        film.setId(0);
        film.setName("");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmDescription() {
        final Film film = new Film();
        film.setId(0);
        film.setName("name");
        film.setDescription(String.format("%201s","s"));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmReleaseDate() {
        final Film film = new Film();
        film.setId(0);
        film.setName("name");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1895,12,27));
        film.setDuration(120);

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }

    @Test
    void shouldBeFalseInvalidFilmDuration() {
        final Film film = new Film();
        film.setId(0);
        film.setName("name");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-1);

        final boolean actual = service.isValid(film);

        assertFalse(actual);
    }
}
