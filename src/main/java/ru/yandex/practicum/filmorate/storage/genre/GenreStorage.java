package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenreStorage {

    void addFilmGenre(Film film);

    void updateFilmGenre(Film oldFilm, Film newFilm);

    LinkedHashSet<Genre> getGenresOneFilm(int id);

    List<Genre> getAllGenre();

    Genre findGenreById(int genreId);

}