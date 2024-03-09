package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        log.info("Добавление нового кинофильма: {}", film);
        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление кинофильма: {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Поиск всех кинофильмов");
        List<Film> filmsList = filmService.getAllFilms();
        log.info("Текущее количество фильмов: {}", filmsList.size());
        return filmsList;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получение кинофильма с id = {}", id);
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с id = {} ставит лайк фильму с id = {}", userId, id);
        return filmService.addFilmLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable int id, @PathVariable int userId)  {
        log.info("Пользователь с id = {} удаляет лайк фильму с id = {}", userId, id);
        return filmService.deleteLikeFilm(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilmsList(@RequestParam(defaultValue = "10", value = "count") int count) {
        List<Film> popularFilmsList = filmService.getPopularFilmsList(count);
        log.info("Формируем список фильмов, с наибольшим числом лайков, в максимальном количестве: {}", count);
        return popularFilmsList;
    }

}