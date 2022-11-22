package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.UserActivity;

import java.util.List;
import java.util.Map;

public interface ActivityStorage {
    void create(Map<String, Object> map);

    List<UserActivity> getAllByUserId(Long id);

    UserActivity getActivityByIdAndEventType(String eventType, Long id);
}
