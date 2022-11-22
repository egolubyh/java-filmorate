package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sqlQuery = "SELECT * " +
                " FROM ACTIVITY WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUserActivity, id);
    }

    @Override
    public UserActivity getActivityByIdAndEventType(String eventType, Long id) {
        String sqlQuery = "SELECT * " +
                " FROM ACTIVITY WHERE ENTITY_ID = ? AND EVENT_TYPE = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUserActivity, id, eventType);

    }

    private UserActivity mapRowToUserActivity(ResultSet rs, int rowNum) throws SQLException {
        UserActivity userActivity = UserActivity.builder()
                .eventId(rs.getLong("event_id"))
                .userId(rs.getLong("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(Operation.valueOf(rs.getString("operation")))
                .entityId(rs.getLong("entity_id"))
                .timestamp(rs.getLong("timestamp"))
                .build();
        return userActivity;
    }
}
