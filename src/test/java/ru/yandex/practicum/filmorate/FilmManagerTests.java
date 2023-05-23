package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmManagerTests {
    private FilmManager filmManager;
    private FilmValidation filmValidation;

    @BeforeEach
    public void beforeEach() {
        filmManager = new FilmManager();
        filmValidation = new FilmValidation();
    }

   @Test
    void testValidationNullName() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            Film filmOne = new Film(null, "Фильм", LocalDate.of(2023, 5, 15),120);
        });
        Assertions.assertEquals("name is marked non-null but is null", exception.getMessage());
    }

    @Test
    void testValidationBlankName() {
        Film filmOne = new Film("", "Фильм", LocalDate.of(2023, 5, 15),120);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmValidation.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Название фильма отсутствует.", exception.getMessage());
    }

    @Test
    void testValidationUnformatDescription() {
        Film filmOne = new Film("Кино", "Фильм", LocalDate.of(2023, 5, 15),120);
        filmOne.setDescription("ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "ФильмФильмФильмФильмФильмФильмФильмФильмФильмФильм" +
                "Фильм");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmValidation.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Описание фильма превышает допустимую длину строки.", exception.getMessage());
    }

    @Test
    void testValidationReleaseDate() {
        Film filmOne = new Film("Кино", "Фильм", LocalDate.of(1800, 1, 1), 120);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmValidation.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Дата выхода фильма раньше даты выхода самого первого фильма.", exception.getMessage());
    }

    @Test
    void testValidationNegativeDuration() {
        Film filmOne = new Film("Кино", "Фильм", LocalDate.of(2023, 5, 15),-1000);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmValidation.validateFilmData(filmOne);
        });
        Assertions.assertEquals("Длительность фильма не может быть неположительным числом.", exception.getMessage());
    }

    @Test
    public void testPutNewFilmInMap() {
        Film filmOne = new Film("Кино", "Фильм", LocalDate.of(2023, 5, 15),120);
        filmManager.putNewFilmInMap(filmOne);
        boolean containsKeyInMap = filmManager.getAllFilms().contains(filmOne);
        Assertions.assertTrue(containsKeyInMap, "Фильм, который нужно было добавить, отсутствует в списке фильмов.");
    }

    @Test
    public void testGetListFilms() {
        Film filmOne = new Film("Кино", "Фильм", LocalDate.of(2023, 5, 15),120);
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
    public void testGetListFilmsNull() {
        List<Film> allFilms = filmManager.getAllFilms();
        assertTrue(allFilms.isEmpty(), "Ложный результат, список фильмов должен быть пуст.");
    }

    @Test
    public void testUpdateMissingFilm() {
        Film filmOne = new Film("Кино", "Фильм", LocalDate.of(2023, 5, 15),120);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            filmManager.updateFilm(filmOne);
        });
        Assertions.assertEquals("Фильм, который необходимо обновить, отсутствует.", exception.getMessage());
    }

}