package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
@Slf4j
public class MpaDbStorage  implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Поиск в БД всех кинорейтингов");
        String selectMpa =
                "SELECT id, name " +
                        "FROM mpa";
        List<Mpa> mpaList = new ArrayList<>();

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(selectMpa);
        while (mpaRows.next()) {
            mpaList.add(mapRowMpa(mpaRows));
        }
        log.info("В БД найдено {} кинорейтингов", mpaList.size());
        return mpaList;
    }

    @Override
    public Mpa findMpaById(int mpaId) {
        log.info("Поиск в БД кинорейтинга с id = {}", mpaId);
        String selectOneMpa =
                "SELECT id, name " +
                        "FROM mpa " +
                        "WHERE id = ?";

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(selectOneMpa, mpaId);
        if(mpaRows.next()) {
            Mpa mpa = mapRowMpa(mpaRows);
            log.info("В БД найден кинорейтинг {} c id = {}", mpa.getName(), mpa.getId());
            return mpa;
        } else {
            throw new FilmNotFoundException(String.format("Кинорейтинг с с id = %s в БД не найден.", mpaId));
        }
    }

    private Mpa mapRowMpa(SqlRowSet mpaRows) {
        int id = mpaRows.getInt("id");
        String name = mpaRows.getString("name");

        Mpa mpa = Mpa.builder()
                .id(id)
                .name(name)
                .build();
        return mpa;
    }

}