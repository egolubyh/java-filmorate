package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void createUser() {
        final User user = new User();
        user.setName("name");
        user.setLogin("log");
        user.setEmail("a@a");
        user.setBirthday(LocalDate.of(1990,1,1));
        final long id = userDbStorage.createUser(user).getId();
        final User user1 = userDbStorage.readUser(id);

        assertEquals(user,user1);
    }

    @Test
    void readUser() {
        final User user = userDbStorage.readUser(1);

        assertEquals(1,user.getId());
        assertEquals("Алексей",user.getName());
        assertEquals("alex",user.getLogin());
        assertEquals("alex@mail.com",user.getEmail());
        assertEquals(LocalDate.of(1991,1,1),user.getBirthday());
    }

    @Test
    void readAllUsers() {
        final List<User> users = userDbStorage.readAllUsers();
        final User user = users.get(0);

        assertEquals(1,user.getId());
        assertEquals("Алексей",user.getName());
        assertEquals("alex",user.getLogin());
        assertEquals("alex@mail.com",user.getEmail());
        assertEquals(LocalDate.of(1991,1,1),user.getBirthday());

        assertEquals(10, users.size());
    }

    @Test
    void readAllFriends() {
        final List<User> friends = userDbStorage.readAllFriends(1);

        assertEquals(3, friends.size());
        assertEquals(3, friends.get(1).getId());
    }

    @Test
    void updateUser() {
        final User user = new User();

        user.setId(4);
        user.setName("up");
        user.setLogin("up");
        user.setEmail("a@a");
        user.setBirthday(LocalDate.of(1990,1,1));

        userDbStorage.updateUser(user);

        final User user1 = userDbStorage.readUser(4);

        assertEquals(user, user1);
    }

    @Test
    void deleteUser() {
        userDbStorage.deleteUser(5);

        assertEquals(9, userDbStorage.readAllUsers().size());
        assertTrue(userDbStorage.idNotExist(5));
    }

    @Test
    void idNotExist() {
        assertTrue(userDbStorage.idNotExist(999));
    }
}