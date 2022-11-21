package ru.yandex.practicum.filmorate.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import ru.yandex.practicum.filmorate.controller.LikeController;
import ru.yandex.practicum.filmorate.controller.MpaController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserActionsInterceptor implements HandlerInterceptor {

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ActivityService activityService;

    private final Map<Class<?>, EventType> eventTypeMap = Map.ofEntries(
            Map.entry(LikeController.class, EventType.LIKE),
            Map.entry(UserController.class, EventType.FRIEND)
    );

    private final Map<String, Operation> nonReviewOperationsMap = Map.ofEntries(
            Map.entry("PUT", Operation.ADD),
            Map.entry("DELETE", Operation.REMOVE)
    );

    private final Map<String, Operation> reviewOperationsMap = Map.ofEntries(
            Map.entry("POST", Operation.ADD),
            Map.entry("PUT", Operation.UPDATE),
            Map.entry("DELETE", Operation.REMOVE)
    );


    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {


        String method = request.getMethod();
        int httpStatus = response.getStatus();
        Class<?> controllerClass = ((HandlerMethod) handler).getBean().getClass();

        String s = getParamsFromPost(request);

        Film f = objectMapper.readValue(s, Film.class);


        boolean needLogEvent = needLogEvent(method, httpStatus, controllerClass);

        if (needLogEvent) {
            if (controllerClass.isAssignableFrom(LikeController.class) || controllerClass.isAssignableFrom(UserController.class)) {

                UserActivity activity = createActivity(controllerClass, method, request);
                activityService.createActivity(activity);
            }
        }

    }

    private Map<String, Operation> getOperationsMap(Class<?> clazz) {
        if (clazz.isAssignableFrom(MpaController.class)) {
            return reviewOperationsMap;
        } else {
            return nonReviewOperationsMap;
        }
    }

    private Operation getOperation(Class<?> clazz, String HttpMethod) {
        Map<String, Operation> operationMap = getOperationsMap(clazz);
        return operationMap.get(HttpMethod);
    }

    private UserActivity createActivity(Class<?> clazz, String HttpMethod, HttpServletRequest request) {

        EventType eventType = eventTypeMap.get(clazz);
        Operation operation = getOperation(clazz, HttpMethod);
        Map<String, Long> attributes = getAttributes(clazz, request);

        return UserActivity.builder()
                .userId(attributes.get("userId"))
                .entityId(attributes.get("entityId"))
                .eventType(eventType)
                .operation(operation)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    private boolean needLogEvent(String httpMethod, int httpStatus, Class<?> clazz) {
        if (httpStatus != 200) {
            return false;
        }

        if (!(httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("DELETE"))) {
            return false;
        }

        if (clazz.isAssignableFrom(LikeController.class) || clazz.isAssignableFrom(UserController.class)) {
            return httpMethod.equals("PUT") || httpMethod.equals("DELETE");
        }
        return true;
    }

    private Map<String, Long> getAttributes(Class<?> clazz, HttpServletRequest request) {
        if (clazz.isAssignableFrom(LikeController.class)) {
            Map<String, String> attributeMap =
                    objectMapper.convertValue(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE), Map.class);
            return Map.ofEntries(
                    Map.entry("userId", Long.parseLong(attributeMap.get("userId"))),
                    Map.entry("entityId", Long.parseLong(attributeMap.get("id")))
            );
        }

        if (clazz.isAssignableFrom(UserController.class)) {
            Map<String, String> attributeMap =
                    objectMapper.convertValue(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE), Map.class);
            return Map.ofEntries(
                    Map.entry("userId", Long.parseLong(attributeMap.get("id"))),
                    Map.entry("entityId", Long.parseLong(attributeMap.get("friendId")))
            );
        }

        return null;
    }

    private String getParamsFromPost(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line + "\n");
            line = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }
}
