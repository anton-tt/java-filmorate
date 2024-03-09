package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
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
    private int rate;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private List<Integer> like = new ArrayList<>();

}