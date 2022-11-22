package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class ReviewController {
    private final ReviewService reviewService;
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Получение всех обзоров, или обзоров на определённый фильм
     */
    @GetMapping("/reviews")
    public List<Review> findAllReviews(@RequestParam (required = false) Optional<Integer> filmId
            ,@RequestParam(required = false)Optional<Integer> count) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /reviews, метод GET");
        return reviewService.readALlReviews(filmId.orElse(null),count.orElse(10));
    }
    /**
     * Получение обзора по id
     */
    @GetMapping("/reviews/{id}")
    public Review findReviewsById(@PathVariable long id
            , @RequestParam(required = false)Optional<Integer> count) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /reviews/{id}, метод GET");
        return reviewService.readReview(id,count.orElse(10));
    }
    /**
     * Создание обзора
     */
    @PostMapping("/reviews")
    public Review createReview(@RequestBody Review review) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /reviews, метод POST");
        return reviewService.createReview(review);
    }
    /**
     * Обновление обзора
     */
    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review review) throws NotFoundException{
        log.info("Получен запрос к эндпоинту: /reviews, метод PUT");
        return reviewService.updateReview(review);
    }
    /**
     * Удаление обзора
     */
    @DeleteMapping ("/reviews/{id}")
    public  void deleteReview(@PathVariable long id){
        log.info("Получен запрос к эндпоинту: /reviews, метод DELETE");
        reviewService.deleteReview(id);
    }
    /**
     * Добавление лайков/дизлайков обзорам
     */
    @PutMapping("/reviews/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable long id,@PathVariable long userId) {
        log.info("Получен запрос к эндпоинту: /reviews/likes, метод PUT");
        reviewService.addLikeToReview(id);
    }
    @PutMapping("/reviews/{id}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable long id,@PathVariable long userId) {
        log.info("Получен запрос к эндпоинту: /reviews/dislikes, метод PUT");
        reviewService.addDislikeToReview(id);
    }
    /**
     * Удаление лайков/дизлайков обзорам
     */
    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void deleteLikeToReview(@PathVariable long id,@PathVariable long userId) {
        log.info("Получен запрос к эндпоинту: /reviews/likes, метод DELETE");
        reviewService.addDislikeToReview(id);
    }
    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void deletelikeToReview(@PathVariable long id,@PathVariable long userId) {
        log.info("Получен запрос к эндпоинту: /reviews/dislikes, метод DELETE");
        reviewService.addLikeToReview(id);
    }
}
