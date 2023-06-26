package ru.yandex.practicum.filmorate.storage.likes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmDataConflictsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Slf4j
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmLike(int filmId, int userId) {
        log.info("Сохранение в БД лайка кинофильму c id = {} пользователя с id = {}.",  filmId, userId);
        String INSERT_LIKE =
                "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";

        int numberModifiedRows = jdbcTemplate.update(INSERT_LIKE,
            filmId,
            userId);
        if (numberModifiedRows > 0) {
            log.info("В БД сохранён лайк кинофильму c id = {} пользователя с id = {}.", filmId, userId);
        } else {
            throw new FilmDataConflictsException(String.format("При сохранении в БД " +
                "лайка кинофильму с id = %s возникла ошибка!", filmId));
        }
    }

    @Override
    public void deleteFilmLike(int filmId, int userId) {
        log.info("Удаление из БД лайка кинофильму c id = {} пользователя с id = {}.", filmId, userId);
        String DELETE_LIKE =
                "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";

        int numberModifiedRows = jdbcTemplate.update(DELETE_LIKE,
            filmId,
            userId);
        if (numberModifiedRows > 0) {
            log.info("Из БД удалён лайк кинофильму c id = {} пользователя с id = {}.", filmId, userId);
        } else {
            throw new FilmDataConflictsException(String.format("При удалении из БД " +
               "лайка кинофильму с id = %s возникла ошибка!", filmId));
        }
    }

    @Override
    public List<Integer> getLikesOneFilm(int filmId) {
        log.info("Поиск в БД всех лайков кинофильма c id = {}.", filmId);
        String SELECT_GENRES =
                "SELECT user_id " +
                        "FROM likes " +
                        "WHERE film_id = ?";

        List<Integer> likeList = new ArrayList<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(SELECT_GENRES, filmId);
        while (likeRows.next()) {
            int userId = likeRows.getInt("user_id");
            likeList.add(userId);
        }
        log.info("В БД найдено {} лайков для кинофильма с id = {}", likeList.size(), filmId);
        return likeList;
    }

}