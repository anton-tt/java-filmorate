package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmManager filmManager = new FilmManager();
    FilmValidation filmValidation = new FilmValidation();

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        filmValidation.validateFilmData(film);
        log.info("Добавлен новый фильм: {}", film);
        return filmManager.putNewFilmInMap(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        filmValidation.validateFilmData(film);
        log.info("Обновлены данные фильма: {}", film);
        return filmManager.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmManager.getAllFilms();
        log.info("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

}