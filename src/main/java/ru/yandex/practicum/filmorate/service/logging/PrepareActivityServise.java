package ru.yandex.practicum.filmorate.service.logging;

import org.springframework.lang.Nullable;
import ru.yandex.practicum.filmorate.model.UserActivity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PrepareActivityServise {

    Map<String, Long> prepareActivityAttributes(HttpServletRequest request);
}
