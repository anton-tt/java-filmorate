package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Director {

    private int id;
    @NotBlank(message = "Отсутствует имя режиссёра.")
    @Size(min = 3, max = 120, message = "Количество символов в имени режиссёра должно быть в интервале " +
            "от 3 до 120 символов.")
    private String name;

}