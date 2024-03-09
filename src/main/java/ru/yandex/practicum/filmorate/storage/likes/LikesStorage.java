package ru.yandex.practicum.filmorate.storage.likes;

import java.util.List;

public interface LikesStorage {

    void addFilmLike(int filmId, int userId);

    void deleteFilmLike(int filmId, int userId);

    List<Integer> getLikesOneFilm(int filmId);

}