package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmManager {
    static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);
    private int nextFilmId = 1;
    private final Map<Integer, Film> filmsMap = new HashMap<>();

    private int getNextId() {
        return nextFilmId++;
    }

    public Film putNewFilmInMap(Film film) {
        film.setId(getNextId());
        filmsMap.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        int filmId = film.getId();
        if (filmsMap.containsKey(filmId)) {
            filmsMap.remove(filmId);
            film.setId(filmId);
            filmsMap.put(filmId, film);
            return film;
        } else {
            throw new RuntimeException("Фильм, который необходимо обновить, отсутствует.");
        }
    }

    public List<Integer> getAllFilms() {
        List<Integer> filmsList = new ArrayList<>();
        if (!filmsMap.isEmpty()) {
            for (Map.Entry<Integer, Film> entry : filmsMap.entrySet()) {
                filmsList.add(entry.getKey());
            }
        }
        return filmsList;
    }

    public void validateFilmData(Film film) {
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