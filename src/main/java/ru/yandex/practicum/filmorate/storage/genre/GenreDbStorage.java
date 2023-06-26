package ru.yandex.practicum.filmorate.storage.genre;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmDataConflictsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@Data
@Slf4j
public class GenreDbStorage  implements  GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(Film film) {
        int filmId = film.getId();
        String filmName = film.getName();
        log.info("Добавление в БД новых жанров кинофильма {}, id = {}.", filmName, filmId);
        String insertGenre =
                "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES (?, ?)";

        LinkedHashSet<Genre> genreList = film.getGenres();
        if (genreList == null || genreList.isEmpty()) {
            log.info("У кинофильма {}, id = {} не заданы жанры, добавить невозможно.", filmName, filmId);
        } else {
            genreList.forEach(genre -> {
                int numberModifiedRows = jdbcTemplate.update(insertGenre,
                        filmId,
                        genre.getId());
                if (numberModifiedRows > 0) {
                    log.info("В БД сохранён жанр кинофильма {}, id = {}.", filmName, filmId);
                } else {
                    throw new FilmDataConflictsException(String.format("При сохранении в БД" +
                            " новых жанров для кинофильма с id = %s возникла ошибка!", filmId));
                }
            });
        }
    }

    @Override
    public void updateFilmGenre(Film oldFilm, Film newFilm) {
        int filmId = oldFilm.getId();
        log.info("Обновление в БД жанров кинофильма {}, id = {}.", oldFilm.getName(), filmId);

        LinkedHashSet<Genre> oldGenreList = oldFilm.getGenres();
        if (oldGenreList != null && !oldGenreList.isEmpty()) {
            deleteFilmGenre(oldFilm);
        } else {
            log.info("У обновляемого фильма с id = {} ранее не были заданы жанры, в БД жанры не удаляем", filmId);
        }

        LinkedHashSet<Genre> newGenreList = newFilm.getGenres();
        if (newGenreList != null && !newGenreList.isEmpty()) {
            addFilmGenre(newFilm);
        } else {
            log.info("У обновлённого фильма с id = {} не заданы жанры, в БД жанры не добавляем", filmId);
        }
    }

    public void deleteFilmGenre(Film film) {
        int filmId = film.getId();
        String filmName = film.getName();
        log.info("Удаление из БД жанров кинофильма {}, id = {}.", filmName, filmId);
        LinkedHashSet<Genre> genreList = film.getGenres();

        if (genreList == null || genreList.isEmpty()) {
            log.info("У кинофильма {}, id = {} не заданы жанры, их удалить нельзя.", filmName, filmId);
        } else {
            String deleteGenre =
                "DELETE FROM film_genre " +
                "WHERE film_id = ?";

            int numberModifiedRows = jdbcTemplate.update(deleteGenre, filmId);
            if (numberModifiedRows >= 0) {
                log.info("Из БД удалены жанры кинофильма {}, id = {}.", filmName, filmId);
            } else {
                throw new FilmDataConflictsException(String.format("При удалении из БД " +
                        "жанров кинофильма с id = %s возникла ошибка!", filmId));
            }
        }
    }

    @Override
    public LinkedHashSet<Genre> getGenresOneFilm(int id) {
        log.info("Получение из БД всех жанров для кинофильма с id = {}", id);
        String selectGenres =
                "SELECT id, name " +
                "FROM genre " +
                "WHERE id IN " +
                        "(SELECT genre_id " +
                                   "FROM film_genre " +
                                   "WHERE film_id = ?)";
        LinkedHashSet<Genre> genreList = new LinkedHashSet<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(selectGenres, id);
        while (genreRows.next()) {
            genreList.add(mapRowGenre(genreRows));
        }
        log.info("В БД найдено {} жанров для кинофильма с id = {}", genreList.size(), id);
        return genreList;
    }

    @Override
    public List<Genre> getAllGenre() {
        log.info("Поиск в базе всех киножанров");
        String selectGenres =
                "SELECT id, name " +
                "FROM genre";
        List<Genre> genreList = new ArrayList<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(selectGenres);
        while (genreRows.next()) {
            genreList.add(mapRowGenre(genreRows));
        }
        log.info("В базе данных найдено {} киножанров", genreList.size());
        return genreList;
    }

    @Override
    public Genre findGenreById(int genreId) {
        log.info("Поиск в БД киножанра с id = {}", genreId);
        String selectOneGenre =
                "SELECT id, name " +
                        "FROM genre " +
                        "WHERE id = ?";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(selectOneGenre, genreId);
        if(genreRows.next()) {
            Genre genre = mapRowGenre(genreRows);
            log.info("В БД найден киножанр {} c id = {}", genre.getName(), genre.getId());
            return genre;
        } else {
            throw new FilmNotFoundException(String.format("Киножанр с с id = %s в БД не найден.", genreId));
        }
    }

    private Genre mapRowGenre(SqlRowSet genreRows) {
        int id = genreRows.getInt("id");
        String name = genreRows.getString("name");

        Genre genre = Genre.builder()
                .id(id)
                .name(name)
                .build();
        return genre;
    }
}
