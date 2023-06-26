package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

@SpringBootTest
class FilmManagerTests {
    private FilmStorage filmStorage;

    @BeforeEach
    public void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
    }


}