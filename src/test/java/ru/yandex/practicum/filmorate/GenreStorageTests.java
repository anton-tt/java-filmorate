package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTests {
    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void addFilmGenreTest() {
        Genre oneGenre = Genre.builder().id(1).name("Комедия").build();
        Genre twoGenre = Genre.builder().id(2).name("Драма").build();
        LinkedHashSet<Genre> genresFilm = new LinkedHashSet<>();
        genresFilm.add(oneGenre);
        genresFilm.add(twoGenre);
        Film dataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .genres(genresFilm)
                .build();
        Film film = filmDbStorage.addNewFilm(dataFilm);
        int filmId = film.getId();
        genreDbStorage.addFilmGenre(film);
        LinkedHashSet<Genre> genres =  genreDbStorage.getGenresOneFilm(filmId);

        assertThat(genres.size()).isEqualTo(2);
    }

    @Test
    public void updateFilmGenreTest() {
        Genre oneGenre = Genre.builder().id(1).name("Комедия").build();
        Genre twoGenre = Genre.builder().id(2).name("Драма").build();
        LinkedHashSet<Genre> startGenresFilm = new LinkedHashSet<>();
        startGenresFilm.add(oneGenre);
        startGenresFilm.add(twoGenre);
        Film startDataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .genres(startGenresFilm)
                .build();
        Film startFilm = filmDbStorage.addNewFilm(startDataFilm);
        int filmId = startFilm.getId();
        genreDbStorage.addFilmGenre(startFilm);

        Genre threeGenre = Genre.builder().id(3).name("Мультфильм").build();
        LinkedHashSet<Genre> newGenresFilm = new LinkedHashSet<>();
        newGenresFilm.add(threeGenre);
        Film newDataFilm = Film.builder()
                .id(filmId)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .genres(newGenresFilm)
                .build();
        genreDbStorage.updateFilmGenre(startFilm, newDataFilm);
        LinkedHashSet<Genre> newGenres =  genreDbStorage.getGenresOneFilm(filmId);

        assertThat(newGenres.size()).isEqualTo(1);
    }

    @Test
    public void deleteFilmGenreTest() {
        Genre twoGenre = Genre.builder().id(2).name("Драма").build();
        LinkedHashSet<Genre> genresFilm = new LinkedHashSet<>();
        genresFilm.add(twoGenre);
        Film dataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .genres(genresFilm)
                .build();
        Film film = filmDbStorage.addNewFilm(dataFilm);
        int filmId = film.getId();
        genreDbStorage.addFilmGenre(film);

        genreDbStorage.deleteFilmGenre(film);
        LinkedHashSet<Genre> newGenres =  genreDbStorage.getGenresOneFilm(filmId);
        assertThat(newGenres.size()).isEqualTo(0);
    }

    @Test
    public void getAllGenreTest() {
        List<Genre> genreList = genreDbStorage.getAllGenre();
        assertThat(genreList.size()).isEqualTo(6);
    }

    @Test
    public void findMpaByIdTest() {
        Genre secondGenre = genreDbStorage.findGenreById(2);
        assertThat(secondGenre).hasFieldOrPropertyWithValue("name", "Драма");

        assertThrows(FilmNotFoundException.class, () -> genreDbStorage.findGenreById(0));
    }

}