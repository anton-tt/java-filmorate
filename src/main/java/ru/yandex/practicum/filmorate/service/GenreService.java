package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }

    public Genre findGenreById(int genreId) {
        if (genreId > 0) {
            return genreStorage.findGenreById(genreId);
        } else {
            throw new FilmNotFoundException(String.format("Киножанр с с id = %s в БД не найден.", genreId));
        }
    }
}