package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmManager {
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

    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>();
        if (!filmsMap.isEmpty()) {
            for (Map.Entry<Integer, Film> entry : filmsMap.entrySet()) {
                filmsList.add(entry.getValue());
            }
        }
        return filmsList;
    }

}