package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {
    private final UserDbStorage userDbStorage;

    @AfterEach
    public void afterEach() {
        List<User> allUsers = userDbStorage.getAllUsers();
        if (!allUsers.isEmpty()) {
            for (User user : allUsers) {
                userDbStorage.deleteUser(user.getId());
            }
        }
    }

    @Test
    public void addNewUserTest() {
        User dataUser = User.builder()
                .id(0)
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User user = userDbStorage.addNewUser(dataUser);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotEqualTo(0);
        assertThat(user).hasFieldOrPropertyWithValue("login", "ant");
    }

    @Test
    public void updateUserTest() {
        User oldDataUser = User.builder()
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User oldUser = userDbStorage.addNewUser(oldDataUser);
        int userId = oldUser.getId();

        User newDataUser = User.builder()
                .id(userId)
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User newUser = userDbStorage.updateUser(newDataUser);

        assertThat(newUser).isNotNull();
        assertThat(newUser).hasFieldOrPropertyWithValue("id", userId)
                .hasFieldOrPropertyWithValue("login", "sts");
    }

    @Test
    public void findUserByIdTest() {
        User dataUser = User.builder()
                .id(0)
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User originalUser = userDbStorage.addNewUser(dataUser);
        int userId = originalUser.getId();

        User foundUser = userDbStorage.findUserById(userId);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isNotEqualTo(0);
        assertThat(foundUser).hasFieldOrPropertyWithValue("login", "ant");
    }

    @Test
    public void getAllUsersTest() {
        User dataOneUser  = User.builder()
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User oneUser = userDbStorage.addNewUser(dataOneUser);

        User dataTwoUser = User.builder()
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User twoUser = userDbStorage.addNewUser(dataTwoUser);

        List<User> allUsers = userDbStorage.getAllUsers();

        assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isNotEqualTo(0);
    }

    @Test
    public void deleteUserTest() {
        User dataUser = User.builder()
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User originalUser = userDbStorage.addNewUser(dataUser);
        int userId = originalUser.getId();

        userDbStorage.deleteUser(userId);
        List<User> allUsers = userDbStorage.getAllUsers();

        assertThat(allUsers.size()).isEqualTo(0);
    }

}