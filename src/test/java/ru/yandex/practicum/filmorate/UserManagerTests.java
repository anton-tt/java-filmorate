package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@SpringBootTest
class UserManagerTests {
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new InMemoryUserStorage();
    }

}