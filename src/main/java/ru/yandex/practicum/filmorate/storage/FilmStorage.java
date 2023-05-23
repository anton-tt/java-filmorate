package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film putNewFilmInMap(Film film);
    Film updateFilm(Film film);
    List<Film> getAllFilms();

}
