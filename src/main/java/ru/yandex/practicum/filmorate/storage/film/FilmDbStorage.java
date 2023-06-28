package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmDataConflictsException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

@Repository
@Primary
@Data
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addNewFilm(Film film) {
        String filmName = film.getName();
        log.info("Добавление в БД нового кинофильма {}.", filmName);
        String insertFilm =
                "INSERT INTO films (name, description, release_date, duration, rate, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int numberModifiedRows = jdbcTemplate.update( (Connection connection) -> {
            PreparedStatement stmt = connection.prepareStatement(insertFilm, new String[]{"id"});
            stmt.setString(1, filmName);
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        if (numberModifiedRows > 0) {
            int filmGeneratedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            film.setId(filmGeneratedId);
            log.info("В БД добавлен новый кинофильм {}, id = {}.", filmName, film.getId());
            return film;
        } else {
            throw new FilmDataConflictsException(String.format("При сохранении в БД " +
                    "нового кинофильма с name = %s возникла ошибка!", filmName));
        }
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        String filmName = film.getName();
        log.info("Обновление в БД кинофильма {}, id = {}.", filmName, filmId);
        String updateFilm =
                "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? " +
                "WHERE id = ?";

        int numberModifiedRows = jdbcTemplate.update(updateFilm,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        if (numberModifiedRows > 0) {
            log.info("В БД обновлён кинофильм {}, id = {}.", filmName, filmId);
            return film;
        } else {
            throw new FilmDataConflictsException(String.format("При обновлении в БД " +
                    "кинофильма с id = %s возникла ошибка!", filmId));
        }
    }

    @Override
    public Film findFilmById(int id) {
        log.info("Поиск в БД кинофильма с id = {}", id);
        String selectFilm =
                "SELECT id, name, description, release_date, duration, rate, mpa_id " +
                "FROM films " +
                "WHERE id = ?";

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(selectFilm, id);
        if (filmRows.next()) {
            Film film = mapRowFilm(filmRows);
            log.info("В БД найден кинофильм {} c id = {}", film.getName(), id);
            return film;
        } else {
            throw new FilmNotFoundException(String.format("Кинофильм с id = %s в БД не найден.", id));
        }
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Поиск в БД всех кинофильмов");
        String selectFilms =
                "SELECT id, name, description, release_date, duration, rate, mpa_id " +
                "FROM films";
        List<Film> filmsList = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(selectFilms);
        while (filmRows.next()) {
            filmsList.add(mapRowFilm(filmRows));
        }
        log.info("В БД найдено {} кинофильмов", filmsList.size());
        return filmsList;
    }

    @Override
    public void deleteFilm(int id) {
        log.info("Удаление из БД кинофильма с id = {}", id);
        String deleteFilm =
                "DELETE FROM films " +
                "WHERE id = ?";

        int numberModifiedRows = jdbcTemplate.update(deleteFilm, id);
        if (numberModifiedRows > 0) {
            log.info("Из БД удалён кинофильм с id = {}", id);
        } else {
            throw new FilmNotFoundException(String.format("Кинофильм с id = %s в БД не найден.", id));
        }
    }

    private Film mapRowFilm(SqlRowSet filmRows) {
        int id = filmRows.getInt("id");
        String name = filmRows.getString("name");
        String description = filmRows.getString("description");
        LocalDate releaseDate = Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate();
        int duration = filmRows.getInt("duration");
        int rate = filmRows.getInt("rate");
        int mpaId = filmRows.getInt("mpa_id");

        Mpa mpa = Mpa.builder()
                .id(mpaId)
                .build();

        Film film = Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .rate(rate)
                .mpa(mpa)
                .build();
        film.setId(id);
        return film;
    }

}