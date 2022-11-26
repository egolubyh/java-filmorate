package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.ActivityService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@RestController
public class FriendController {

    private final UserStorage userStorage;
    private final UserService userService;
    private final ValidationService validationService;

    private final ActivityService activityService;

    @Autowired
    public FriendController(@Qualifier("userDbStorage") UserStorage userStorage,
                          UserService userService,
                          ValidationService validationService,
                          ActivityService activityService) {
        this.userStorage = userStorage;
        this.userService = userService;
        this.validationService = validationService;
        this.activityService = activityService;
    }

    /**
     * Добавление в друзья.
     * @param id идентификатор пользователя.
     * @param friendId идентификатор друга.
     * @throws NotFoundException если пользователя или друга с таким id не существует.
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable long id,
                           @PathVariable long friendId) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends/{friendId}, метод PUT");
        if (userStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, пользователя с таким id = " + id + " не существует.");
        }
        if (userStorage.idNotExist(friendId)) {
            throw new NotFoundException(friendId,"Ошибка, пользователя с таким id = " + friendId + " не существует.");
        }

        userService.addFriend(id,friendId);
    }

    /**
     * Удаление из друзей.
     * @param id идентификатор пользователя.
     * @param friendId идентификатор друга.
     * @throws NotFoundException если пользователя или друга с таким id не существует.
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id,
                              @PathVariable int friendId) throws NotFoundException {
        log.info("Получен запрос к эндпоинту: /users/{id}/friends/{friendId}, метод DELETE");
        if (userStorage.idNotExist(id)) {
            throw new NotFoundException(id,"Ошибка, пользователя с таким id = " + id + " не существует.");
        }
        if (userStorage.idNotExist(friendId)) {
            throw new NotFoundException(friendId,"Ошибка, пользователя с таким id = " + friendId + " не существует.");
        }

        userService.deleteFriend(id,friendId);
    }
}
