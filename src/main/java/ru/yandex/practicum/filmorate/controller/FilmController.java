package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    FilmManager filmManager = new FilmManager();

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        filmManager.validateFilmData(film);
        log.debug("Добавлен новый фильм: {}", film);
        return filmManager.putNewFilmInMap(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        filmManager.validateFilmData(film);
        log.debug("Обновлённы данные фильма: {}", film);
        return filmManager.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmManager.getAllFilms();
        log.debug("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

}