package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    /**
     * Идентификатор фильма
     */
    long id;
    /**
     * Наименование фильма
     */

    @NotBlank
    String name;
    /**
     * Описание фильма
     */

    @Size(max = 200)
    String description;
    /**
     * Дата выхода фильма в прокат
     */

    LocalDate releaseDate;
    /**
     * Продолжительность фильма в минутах
     */
    @Positive
    long duration;
    /**
     * Лайки для фильма
     */
    @NonFinal
    @Setter
    Set<Long> likes;
    /**
     * Список жанров фильма
     */
    @NonFinal
    @Setter
    List<Genre> genres;
    /**
     * Список режиссёров фильма
     */
    @NonFinal
    @Setter
    List<Director> directors;
    /**
     * Рейтинг фильма
     */
    @NonFinal
    @NonNull
    Mpa mpa;
    /**
     * Оценка фильма
     */
    @NonFinal
    @Setter
    int rate;
}
