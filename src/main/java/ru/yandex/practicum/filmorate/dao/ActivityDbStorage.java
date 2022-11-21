package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ActivityDbStorage implements ActivityStorage{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(Map<String, Object> map) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ACTIVITY")
                .usingGeneratedKeyColumns("EVENT_ID");

        insert.execute(map);
    }

    @Override
    public List<UserActivity> getAllByUserId(Long id) {
        return null;
    }
}
