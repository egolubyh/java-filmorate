package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ActivityStorage;
import ru.yandex.practicum.filmorate.model.UserActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ActivityService {

    @Autowired
    private final ActivityStorage activityStorage;

    public void createActivity(UserActivity activity) {
        activityStorage.create(toMap(activity));
    }

    public List<UserActivity> getAllByUserId(Long userId) {
        return activityStorage.getAllByUserId(userId);
    }

    public UserActivity getActivityByIdAndEventType(String eventType, Long id) {
        return activityStorage.getActivityByIdAndEventType(eventType, id);
    }

    private Map<String, Object> toMap(UserActivity activity) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id", activity.getUserId());
        map.put("event_type", activity.getEventType());
        map.put("operation", activity.getOperation());
        map.put("entity_id", activity.getEntityId());
        map.put("timestamp", activity.getTimestamp());
        return map;
    }
}
