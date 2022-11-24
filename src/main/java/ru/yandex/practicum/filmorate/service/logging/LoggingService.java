package ru.yandex.practicum.filmorate.service.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FriendController;
import ru.yandex.practicum.filmorate.controller.LikeController;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoggingService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ActivityService activityService;

    private final Map<Class<?>, EventType> eventTypeMap = Map.ofEntries(
            Map.entry(LikeController.class, EventType.LIKE),
            Map.entry(FriendController.class, EventType.FRIEND),
            Map.entry(ReviewController.class, EventType.REVIEW)
    );

    private final Map<String, Operation> nonReviewOperationsMap = Map.ofEntries(
            Map.entry("PUT", Operation.ADD),
            Map.entry("DELETE", Operation.REMOVE)
    );

    private final Map<Class<?>, Class<? extends PrepareActivityServise>> controllerBeanMap = Map.ofEntries(
            Map.entry(FriendController.class, PrepareFriendActivity.class),
            Map.entry(LikeController.class, PrepareLikeActivity.class),
            Map.entry(ReviewController.class, PrepareReviewActivity.class)
    );

    private final Map<String, Operation> reviewOperationsMap = Map.ofEntries(
            Map.entry("POST", Operation.ADD),
            Map.entry("PUT", Operation.UPDATE),
            Map.entry("DELETE", Operation.REMOVE)
    );

    public UserActivity createActivity(Class<?> clazz, String HttpMethod, HttpServletRequest request) {

        EventType eventType = eventTypeMap.get(clazz);
        Operation operation = getOperation(clazz, HttpMethod);
        PrepareActivityServise prepareActivityServise = getPrepareActivityService(clazz);
        Map<String, Long> attributes = prepareActivityServise.prepareActivityAttributes(request);

        return UserActivity.builder()
                .userId(attributes.get("userId"))
                .entityId(attributes.get("entityId"))
                .eventType(eventType)
                .operation(operation)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()).toInstant().toEpochMilli())
                .build();
    }

    public void logActivity(UserActivity activity) {
        activityService.createActivity(activity);
    }

    private Operation getOperation(Class<?> clazz, String HttpMethod) {
        Map<String, Operation> operationMap = getOperationsMap(clazz);
        return operationMap.get(HttpMethod);
    }
    private Map<String, Operation> getOperationsMap(Class<?> clazz) {
        if (clazz.isAssignableFrom(ReviewController.class)) {
            return reviewOperationsMap;
        } else {
            return nonReviewOperationsMap;
        }
    }

    private PrepareActivityServise getPrepareActivityService(Class<?> clazz) {
        return applicationContext.getBean(controllerBeanMap.get(clazz));
    }
}
