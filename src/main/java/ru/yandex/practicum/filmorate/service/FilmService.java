package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserDataConflictsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final MpaStorage mpaStorage;

    public Film addNewFilm(Film newFilm) {
        FilmValidation.validateFilmData(newFilm);
        Film film = filmStorage.addNewFilm(newFilm);
        genreStorage.addFilmGenre(film);
        return film;
    }

    public Film updateFilm(Film updatedFilm) {
        FilmValidation.validateFilmData(updatedFilm);

        Film oldFilm = filmStorage.findFilmById(updatedFilm.getId());
        LinkedHashSet<Genre> oldGenreList = genreStorage.getGenresOneFilm(updatedFilm.getId());
        oldFilm.setGenres(oldGenreList);

        Film newFilm = filmStorage.updateFilm(updatedFilm);
        setMpaFilm(newFilm);
        genreStorage.updateFilmGenre(oldFilm, updatedFilm);
        return newFilm;
    }

    public Film findFilmById(int id) {
        Film film = filmStorage.findFilmById(id);
        setMpaFilm(film);
        setLikeFilm(film);
        LinkedHashSet<Genre> genreList = genreStorage.getGenresOneFilm(id);
        film.setGenres(genreList);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> filmList = filmStorage.getAllFilms();
        filmList.forEach(film -> {
            setMpaFilm(film);
            setLikeFilm(film);
            int filmId = film.getId();
            LinkedHashSet<Genre> genreList = genreStorage.getGenresOneFilm(filmId);
            film.setGenres(genreList);
        });
        return filmList;
    }

    public Film addFilmLike(int filmId, int userId) {
        if (userId > 0) {
            Film film = findFilmById(filmId);
            if (film.getLike() != null && film.getLike().contains(userId)) {
                throw new UserDataConflictsException(String.format("Пользователь с id = %s ранее уже поставил лайк" +
                    " этому фильму.", userId));
            } else {
                likesStorage.addFilmLike(filmId, userId);
           }
           return film;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с отрицательным id = %s, " +
                    " добавить лайк невозможно.", userId));
        }
    }

    public Film deleteLikeFilm(int userId, int filmId) {
        if (userId > 0) {
            Film film = findFilmById(filmId);
            List<Integer> likesList = film.getLike();
            if (likesList != null && likesList.contains(userId)) {
                likesStorage.deleteFilmLike(filmId, userId);
            } else {
                throw new UserDataConflictsException(String.format("Пользователь с id = %s не ставил лайк" +
                        " этому фильму, удалить его невозможно.", userId));
            }
            return film;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с отрицательным id = %s, " +
                    " удалить лайк невозможно.", userId));
        }
    }

    public List<Film> getPopularFilmsList(int count) {
        List<Film> filmList = filmStorage.getAllFilms();
        filmList.forEach(film -> {
            setMpaFilm(film);
            int filmId = film.getId();
            LinkedHashSet<Genre> genreList = genreStorage.getGenresOneFilm(filmId);
            film.setGenres(genreList);
            setLikeFilm(film);
        });

        if (filmList.isEmpty()) {
            throw new FilmNotFoundException("Фильмы отсутствуют, сортировка по популярности невозможна.");
        } else {
            Comparator<Film> popularFilmsComparator = (Film filmOne, Film filmTwo) -> {
                int likesFilmOne = 0;
                if (filmOne.getLike() != null) {
                    likesFilmOne = filmOne.getLike().size();
                }
                int likesFilmTwo = 0;
                if (filmTwo.getLike() != null) {
                    likesFilmTwo = filmTwo.getLike().size();
                }
                return Integer.compare(likesFilmTwo, likesFilmOne);
            };

            return filmList.stream()
                    .sorted(popularFilmsComparator)
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    public List<Genre> getAllGenre() {
        List<Genre> genreList = genreStorage.getAllGenre();
        return genreList;
    }

    public Film setMpaFilm(Film film) {
        int mpaId = film.getMpa().getId();
        Mpa newMpa = mpaStorage.findMpaById(mpaId);
        film.setMpa(newMpa);
        return film;
    }

    public Film setLikeFilm(Film film) {
        List<Integer> likeList = likesStorage.getLikesOneFilm(film.getId());
        film.setLike(likeList);
        return film;
    }

}