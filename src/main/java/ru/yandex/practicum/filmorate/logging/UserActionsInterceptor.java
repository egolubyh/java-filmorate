package ru.yandex.practicum.filmorate.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import ru.yandex.practicum.filmorate.controller.FriendController;
import ru.yandex.practicum.filmorate.controller.LikeController;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;
import ru.yandex.practicum.filmorate.service.logging.LoggingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserActionsInterceptor implements HandlerInterceptor {

    @Autowired
    private final LoggingService loggingService;

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

        String method = request.getMethod();
        int httpStatus = response.getStatus();
        Class<?> controllerClass = ((HandlerMethod) handler).getBean().getClass();
        boolean needLogEvent = needLogEvent(method, httpStatus, controllerClass);


        if (needLogEvent) {
            UserActivity activity = loggingService.createActivity(controllerClass, method, request);
            loggingService.logActivity(activity);
        }
    }

    private boolean needLogEvent(String httpMethod, int httpStatus, Class<?> clazz) {
        if (httpStatus != 200) {
            return false;
        }

        if (clazz.isAssignableFrom(ReviewController.class)) {
            return httpMethod.equals("PUT") || httpMethod.equals("DELETE") || httpMethod.equals("POST");
        }

        if (clazz.isAssignableFrom(LikeController.class) || clazz.isAssignableFrom(FriendController.class)) {
            return httpMethod.equals("PUT") || httpMethod.equals("DELETE");
        }
        return false;
    }
}
