package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = mpaStorage.getAllMpa();
        return mpaList;
    }

    public Mpa findMpaById(int mpaId) {
        if (mpaId > 0) {
            Mpa mpa = mpaStorage.findMpaById(mpaId);
            return mpa;
        } else {
            throw new FilmNotFoundException(String.format("Отрицательное значение id = %s рейтинга, " +
                "найти данные невозможно!", mpaId));
        }
    }

}