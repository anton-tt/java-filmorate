package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserDataConflictsException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film likeFilm(int filmId, int userId) {
        UserValidation.validateUserId(userId, userStorage.getUsersMap());
        Film film = filmStorage.getRequiredFilm(filmId);
        Set<Integer> likesFilmsSet = film.getLikes();
        if (likesFilmsSet.contains(userId)) {
            throw new UserDataConflictsException(String.format("Пользователь с id = %s ранее уже поставил лайк" +
                    " этому фильму.", userId));
        } else {
            likesFilmsSet.add(userId);
       }
        return film;
    }

    public Film deleteLikeFilm(int userId, int filmId) {
        UserValidation.validateUserId(userId, userStorage.getUsersMap());
        Film film = filmStorage.getRequiredFilm(filmId);
        Set<Integer> likesFilmsSet = film.getLikes();
        if (!likesFilmsSet.contains(userId)) {
            throw new UserDataConflictsException(String.format("Пользователь с id = %s не ставил лайк фильму," +
                    " удалить лайк невозможно.", userId));
        } else {
            likesFilmsSet.remove(userId);
        }
        return film;
    }

    public List<Film> getPopularFilmsList(int count) {
        List<Film> filmsList = filmStorage.getAllFilms();
        if (filmsList.isEmpty()) {
            throw new FilmNotFoundException("Фильмы отсутствуют, сортировка по популярности невозможна.");
        } else {
            Comparator<Film> popularFilmsComparator = (Film filmOne, Film filmTwo) -> {
                int likesFilmOne = 0;
                if (filmOne.getLikes() != null) {
                    likesFilmOne = filmOne.getLikes().size();
                }
                int likesFilmTwo = 0;
                if (filmTwo.getLikes() != null) {
                    likesFilmTwo = filmTwo.getLikes().size();
                }
                return Integer.compare(likesFilmTwo, likesFilmOne);
            };
            return filmsList.stream()
                    .sorted(popularFilmsComparator)
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

}