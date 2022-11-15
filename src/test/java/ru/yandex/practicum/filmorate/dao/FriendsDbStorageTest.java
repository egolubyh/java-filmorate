package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendsDbStorageTest {
    private final FriendsDbStorage friendsDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    void createFriendship() {
        friendsDbStorage.createFriendship(5,7);

        final User friend = userDbStorage.readAllFriends(5).get(0);

        assertEquals(7, friend.getId());

    }

    @Test
    void deleteFriendship() {
        friendsDbStorage.deleteFriendship(5,7);

        assertEquals(0, userDbStorage.readAllFriends(5).size());
    }
}