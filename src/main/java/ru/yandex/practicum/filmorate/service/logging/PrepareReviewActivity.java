package ru.yandex.practicum.filmorate.service.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;
import ru.yandex.practicum.filmorate.controller.ReviewController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserActivity;
import ru.yandex.practicum.filmorate.service.ActivityService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrepareReviewActivity implements PrepareActivityServise {

    @Autowired
    private final ObjectMapper objectMapper;

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    private final ActivityService activityService;

    @SneakyThrows
    @Override
    public Map<String, Long> prepareActivityAttributes(HttpServletRequest request) {

        if (request.getMethod().equals("DELETE")) {
            Map<String, String> attributeMap =
                    objectMapper.convertValue(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE), Map.class);
            Long reviewId = Long.parseLong(attributeMap.get("id"));

            UserActivity userActivity = activityService.getActivityByIdAndEventType("REVIEW", reviewId);

            return Map.ofEntries(
                    Map.entry("userId", userActivity.getUserId()),
                    Map.entry("entityId", userActivity.getEntityId())
            );
        }

        if (request.getMethod().equals("PUT")) {
            Review req = objectMapper.readValue(request.getReader(), Review.class);
            Review found = reviewService.readReview(req.getReviewId(), 1);

            Long userId = found.getUserId();
            Long entityId = found.getReviewId();

            return Map.ofEntries(
                    Map.entry("userId", userId),
                    Map.entry("entityId", entityId)
            );
        }

        Review req = objectMapper.readValue(request.getReader(), Review.class);

        Long userId = req.getUserId();
        Long filmId = req.getFilmId();

        Review review = reviewService.findReviewByUserAndFilmId(userId, filmId);

        return Map.ofEntries(
                Map.entry("userId", userId),
                Map.entry("entityId", review.getReviewId())
        );
    }
}
