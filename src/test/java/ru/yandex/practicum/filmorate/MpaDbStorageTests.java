package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTests {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getAllMpaTest() {
        List<Mpa> allMpa = mpaDbStorage.getAllMpa();
        assertThat(allMpa.size()).isEqualTo(5);
    }

    @Test
    public void findMpaByIdTest() {
        Mpa secondMpa = mpaDbStorage.findMpaById(2);
        assertThat(secondMpa).hasFieldOrPropertyWithValue("name", "PG");

        assertThrows(FilmNotFoundException.class, () -> mpaDbStorage.findMpaById(0));
    }

}