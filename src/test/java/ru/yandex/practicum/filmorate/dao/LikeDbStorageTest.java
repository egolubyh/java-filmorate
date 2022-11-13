package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeDbStorageTest {
    private final LikeDbStorage likeDbStorage;

    @Test
    void createLike() {
        likeDbStorage.createLike(9,6);
        likeDbStorage.createLike(9,7);
        likeDbStorage.createLike(9,8);
        likeDbStorage.createLike(9,9);
        likeDbStorage.createLike(9,10);

        assertEquals(9, likeDbStorage.findMostPopularId(1).get(0));
    }

    @Test
    void deleteLike() {
        likeDbStorage.deleteLike(9,6);
        likeDbStorage.deleteLike(9,7);
        likeDbStorage.deleteLike(9,8);
        likeDbStorage.deleteLike(9,9);
        likeDbStorage.deleteLike(9,10);
    }

    @Test
    void findMostPopularId() {
        List<Long> list = likeDbStorage.findMostPopularId(3);

        assertEquals(12, list.get(0));
        assertEquals(11, list.get(1));
        assertEquals(7, list.get(2));
    }
}