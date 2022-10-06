package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private InMemoryUserStorage storage;
    private UserService service;

    @BeforeEach
    void setUp() {
        storage = new InMemoryUserStorage();
        service = new UserService(storage);

        final User user = new User(0,"m@","u","u", LocalDate.now(),new HashSet<>()); //id=1
        final User friend = new User(0,"m@","f","f", LocalDate.now(),new HashSet<>()); //id=2
        final User friend1 = new User(0,"m@","f","f", LocalDate.now(),new HashSet<>()); //id=3
        final User friend2 = new User(0,"m@","f","f", LocalDate.now(),new HashSet<>()); //id=4
        final User friend3 = new User(0,"m@","f","f", LocalDate.now(),new HashSet<>()); //id=5
        final User friend4 = new User(0,"m@","f","f", LocalDate.now(),new HashSet<>()); //id=6

        storage.createUser(user);
        storage.createUser(friend);
        storage.createUser(friend1);
        storage.createUser(friend2);
        storage.createUser(friend3);
        storage.createUser(friend4);
    }

    @Test
    void addFriend() {
        service.addFriend(1,2);

        final Set<Integer> user = storage.findUserById(1).getFriends();
        final Set<Integer> friend = storage.findUserById(2).getFriends();

        assertEquals(Set.of(2),user);
        assertEquals(Set.of(1),friend);
    }

    @Test
    void deleteFriend() {
        service.addFriend(1,2);
        service.deleteFriend(2,1);

        final Set<Integer> user = storage.findUserById(1).getFriends();
        final Set<Integer> friend = storage.findUserById(2).getFriends();

        assertTrue(user.isEmpty());
        assertTrue(friend.isEmpty());
    }

    @Test
    void findAllMutualFriends() {
        service.addFriend(1,2);
        service.addFriend(1,3);
        service.addFriend(1,4);
        service.addFriend(1,5);
        service.addFriend(1,6);
        service.addFriend(2,4);
        service.addFriend(2,6);

        final List<Integer> expected = List.of(4,6);
        final List<Integer> actual = service.findAllMutualFriends(1, 2)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());

        assertEquals(expected,actual);
    }
}