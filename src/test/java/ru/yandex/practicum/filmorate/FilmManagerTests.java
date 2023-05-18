package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmManagerTests {
    private FilmManager filmManager;
    private Film filmOne;

    @BeforeEach
    public void beforeEach() {
        filmManager = new FilmManager();
        filmOne = new Film("Кино", "Фильм", LocalDate.of(2023, 5, 15),120);
    }

    @Test
    void validationNullName() {
        filmOne.setName(null);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Название фильма отсутствует.", exception.getMessage());
    }

    @Test
    void validationBlankName() {
        filmOne.setName("");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Название фильма отсутствует.", exception.getMessage());
    }

    @Test
    void validationUnformatDescription() {
        filmOne.setDescription("ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "Фильм");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Описание фильма превышает допустимую длину строки.", exception.getMessage());
    }

    @Test
    void validationReleaseDate() {
        filmOne.setReleaseDate(LocalDate.of(1800, 1, 1));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Дата выхода фильма раньше даты выхода самого первого фильма.", exception.getMessage());
    }

    @Test
    void validationNegativeDuration() {
        filmOne.setDuration(-1000);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Длительность фильма не может быть неположительным числом.", exception.getMessage());
    }

    @Test
    public void putNewFilmInMap() {
        filmManager.putNewFilmInMap(filmOne);
        boolean containsKeyInMap = filmManager.getAllFilms().contains(filmOne);
        Assertions.assertTrue(containsKeyInMap, "Фильм, который нужно было добавить, отсутствует в списке фильмов.");
    }

    @Test
    public void getListFilms() {
        filmManager.putNewFilmInMap(filmOne);
        Film filmTwo = new Film("Кино2", "Фильм", LocalDate.of(2023, 5, 16),120);
        filmManager.putNewFilmInMap(filmTwo);
        List<Film> allFilms = filmManager.getAllFilms();
        boolean empty = allFilms.isEmpty();
        int allFilmsSize = allFilms.size();
        assertFalse(empty, "После добавления двух фильмов список остался пустой.");
        assertEquals(2, allFilmsSize, "Число добавленных фильмов и выводимых в списке не совпадают.");
    }

    @Test
    public void getListFilmsNull() {
        List<Film> allFilms = filmManager.getAllFilms();
        assertTrue(allFilms.isEmpty(), "Ложный результат, список фильмов должен быть пуст.");
    }

    @Test
    public void updateMissingFilm() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.updateFilm(filmOne);
        });
        Assertions.assertEquals("Фильм, который необходимо обновить, отсутствует.", exception.getMessage());
    }

}