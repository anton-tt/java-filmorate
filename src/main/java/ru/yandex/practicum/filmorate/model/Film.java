package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NonNull @NotBlank
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;

}