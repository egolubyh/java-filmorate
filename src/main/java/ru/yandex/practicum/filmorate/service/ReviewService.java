package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.ReviewDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewDbStorage reviewStorage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    @Autowired
    public ReviewService(ReviewDbStorage reviewStorage, FilmDbStorage filmStorage, UserDbStorage userStorage) {
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }
    /**
     * Получение всех обзоров, или обзоров на определённый фильм
     */
    public List<Review> readALlReviews(Integer idFilm,Integer count) throws NotFoundException {
        if(idFilm == null){
            return reviewStorage.readAllReview(count);
        }
        if(filmStorage.idNotExist(idFilm)){
            throw new NotFoundException(idFilm, "Ошибка, фильма с таким id = " + idFilm + " не существует.");
        }
        return reviewStorage.readAllReviewForFilm(idFilm.longValue(),count);

    }
    /**
     * Получение обзора по id
     */
    public Review readReview(Long id, Integer count) throws NotFoundException {
        return reviewStorage.readReview(id, count);
    }
    /**
     * Создание обзора
     */
    public Review createReview(Review review) throws NotFoundException {
        checkExistFilmsAndUser(review.getUserId(), review.getFilmId());
            return reviewStorage.createReview(review);
    }
    /**
     * Обновление обзора
     */
    public Review updateReview(Review review) throws NotFoundException {
        checkExistFilmsAndUser(review.getUserId(), review.getFilmId());
        return reviewStorage.updateReview(review);
    }
    /**
     * Удаление обзора
     */
    public void deleteReview(Long id) {
        reviewStorage.deleteReview(id);
    }
    /**
     * Добавление лайков/дизлайков
     */
    public void addLikeToReview(Long id){
        reviewStorage.addLikeToReview(id);
    }
    public void addDislikeToReview(Long id){
        reviewStorage.addDislikeToReview(id);
    }
    /**
     * Проверка на наличии фильма и пользователя в базе
     */

    /**
     * Получение обзора по id пользователя и id фильма
     */
    public Review findReviewByUserAndFilmId(Long userId, Long filmId) throws NotFoundException {
        return reviewStorage.readReviewByUserIdAndFilmId(userId, filmId);
    }
    private boolean checkExistFilmsAndUser(long userId, long filmId) throws NotFoundException {
        if(filmStorage.idNotExist(filmId)){
            throw new NotFoundException(filmId, "Ошибка, фильма с таким id = " + filmId + " не существует.");
        } else if (userStorage.idNotExist(userId)){
            throw new NotFoundException(filmId, "Ошибка, пользователя с таким id = " + filmId + " не существует.");
        }
        return true;
    }
}
