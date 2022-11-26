package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {
    /**
     * Идентификатор отзыва
     */
    private Long reviewId;
    /**
     *Описание отзыва
     */
    @NotNull
    private String content;
    /**
     * Тип отзыва
     */
    @NotNull
    private Boolean isPositive;
    /**
     * Индентификатор пользователя
     */
    @NotNull
    private Long userId;
    /**
     * Индентификатор фильма
     */
    @NotNull
    private Long filmId;
    /**
     * Рейтинг отзыва
     */
    private long useful = 0;

    @JsonProperty(value="isPositive")
    public boolean isPositive() {
        return isPositive;
    }
}
