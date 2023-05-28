package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

public final class FilmValidation {
    private FilmValidation() {

    }
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);

    public static void validateFilmData(Film film) {
        String filmName = film.getName();
        String filmDescription = film.getDescription();
        LocalDate filmReleaseDate = film.getReleaseDate();
        int filmDuration = film.getDuration();

        if (filmName == null || filmName.isBlank()) {
            throw new ValidationException("Название фильма отсутствует.");
        }
        if (filmDescription.length() > 200) {
            throw new ValidationException("Описание фильма превышает допустимую длину строки.");
        }
        if (filmReleaseDate.isBefore(BIRTHDAY_MOVIE)) {
            throw new ValidationException("Дата выхода фильма раньше даты выхода самого первого фильма.");
        }
        if (filmDuration <= 0) {
            throw new ValidationException("Длительность фильма не может быть неположительным числом.");
        }
    }

}