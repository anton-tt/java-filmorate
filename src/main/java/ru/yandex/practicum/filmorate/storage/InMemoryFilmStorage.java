package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage  implements FilmStorage {
    private int nextFilmId = 1;
    private final Map<Integer, Film> filmsMap = new HashMap<>();

    private int getNextId() {
        return nextFilmId++;
    }

    @Override
    public Film putNewFilmInMap(Film film) {
        film.setId(getNextId());
        filmsMap.put(film.getId(), film);
        return film;
    }

    @Override
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

    @Override
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
