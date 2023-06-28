package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private final FilmDbStorage filmDbStorage;

    @AfterEach
    public void afterEach() {
        List<Film> allFilms = filmDbStorage.getAllFilms();
        if (!allFilms.isEmpty()) {
            for (Film film : allFilms) {
                filmDbStorage.deleteFilm(film.getId());
            }
        }
    }

    @Test
    public void addNewFilmTest() {
        Film dataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film film = filmDbStorage.addNewFilm(dataFilm);

        assertThat(film).isNotNull();
        assertThat(film.getId()).isNotEqualTo(0);
        assertThat(film).hasFieldOrPropertyWithValue("name", "movie");
    }

    @Test
    public void updateFilmTest() {
        Film oldDataFilm = Film.builder()
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film oldFilm = filmDbStorage.addNewFilm(oldDataFilm);
        int filmId = oldFilm.getId();

        Film newDataFilm = Film.builder()
                .id(filmId)
                .name("cinema")
                .description("cinemacinema")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film newFilm = filmDbStorage.updateFilm(newDataFilm);

        assertThat(newFilm).isNotNull();
        assertThat(newFilm).hasFieldOrPropertyWithValue("id", filmId)
                .hasFieldOrPropertyWithValue("name", "cinema");
    }

    @Test
    public void findFilmByIdTest() {
        Film dataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film originalUser = filmDbStorage.addNewFilm(dataFilm);
        int userId = originalUser.getId();

        Film foundFilm = filmDbStorage.findFilmById(userId);

        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getId()).isNotEqualTo(0);
        assertThat(foundFilm).hasFieldOrPropertyWithValue("name", "movie");
    }

    @Test
    public void getAllFilmsTest() {
        Film dataOneFilm  = Film.builder()
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film oneFilm = filmDbStorage.addNewFilm(dataOneFilm);

        Film dataTwoFilm = Film.builder()
                .name("cinema")
                .description("cinemacinema")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film newFilm = filmDbStorage.addNewFilm(dataTwoFilm);

        List<Film> allFilms = filmDbStorage.getAllFilms();

        assertThat(allFilms).isNotNull();
        assertThat(allFilms.size()).isNotEqualTo(0);
    }

    @Test
    public void deleteFilmTest() {
        Film dataFilm = Film.builder()
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film originalFilm = filmDbStorage.addNewFilm(dataFilm);
        int filmId = originalFilm.getId();

        filmDbStorage.deleteFilm(filmId);
        List<Film> allFilms = filmDbStorage.getAllFilms();

        assertThat(allFilms.size()).isEqualTo(0);
    }

}