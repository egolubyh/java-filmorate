package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;

@Data
@Builder
public class UserActivity {
    private Long eventId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @Enumerated(EnumType.STRING)
    private Operation operation;
    private Long entityId;
    private Timestamp timestamp;
}
