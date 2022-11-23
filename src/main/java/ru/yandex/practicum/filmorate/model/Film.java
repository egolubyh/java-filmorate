package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
public class Film {

    long id;

    @NonNull
    @NotBlank
    String name;

    @NonNull
    @Size(max = 200)
    String description;

    @NonNull
    LocalDate releaseDate;

    @Positive
    long duration;

    @NonFinal
    @Setter
    Set<Long> likes;

    @NonFinal
    @Setter
    Set<Genre> genres;

    @NonFinal
    @Setter
    Set<Director> directors;

    @NonFinal
    @NonNull
    Mpa mpa;

    @NonFinal
    @Setter
    int rate;
}
