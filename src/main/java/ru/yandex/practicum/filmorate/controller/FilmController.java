package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        FilmValidation.validateFilmData(film);
        log.info("Добавлен новый фильм: {}", film);
        return filmStorage.putNewFilmInMap(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        FilmValidation.validateFilmData(film);
        log.info("Обновлены данные фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = filmStorage.getAllFilms();
        log.info("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получены данные фильма с id= {}", id);
        return filmStorage.getRequiredFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с id= {} поставил лайк фильму с id= {}", userId, id);
        return filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable int id, @PathVariable int userId)  {
        log.info("Пользователь с id= {} удалил лайк фильму с id= {}", userId, id);
        return filmService.deleteLikeFilm(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilmsList(@RequestParam(defaultValue = "10", value = "count") int count) {
        List<Film> popularFilmsList = filmService.getPopularFilmsList(count);
        log.info("Выведен список фильмов с наибольшим числом лайков в количестве: {}", count);
        return popularFilmsList;
    }

}