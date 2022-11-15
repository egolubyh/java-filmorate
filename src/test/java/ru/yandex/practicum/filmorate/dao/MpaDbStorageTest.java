package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void readMpa() {
        Mpa mpa = mpaDbStorage.readMpa(3);

        assertEquals(3, mpa.getId());
        assertEquals("PG-13", mpa.getName());
    }

    @Test
    void readAllMpa() {
        List<Mpa> list = mpaDbStorage.readAllMpa();

        assertEquals(5, list.size());
        assertEquals("NC-17", list.get(4).getName());
    }

    @Test
    void idNotExist() {
        assertTrue(mpaDbStorage.idNotExist(999));
    }
}