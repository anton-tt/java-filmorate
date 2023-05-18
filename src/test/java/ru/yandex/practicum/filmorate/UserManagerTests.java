package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserManagerTests {
    private UserManager userManager;
    private User userOne;

    @BeforeEach
    public void beforeEach() {
        userManager = new UserManager();
        userOne = new User("ant@", "ant", "Ant", LocalDate.of(1900, 1, 1));
    }

    @Test
    void validationBlankEmail() {
        userOne.setEmail("");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.validateUserData(userOne);
        });
        Assertions.assertEquals("Адрес электронной почты введён в неправильном формате.", exception.getMessage());
    }

    @Test
    void validationUnformatEmail() {
        userOne.setEmail("ant");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.validateUserData(userOne);
        });
        Assertions.assertEquals("Адрес электронной почты введён в неправильном формате.", exception.getMessage());
    }

    @Test
    void validationNullLogin() {
        userOne.setLogin(null);
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.validateUserData(userOne);
        });
        Assertions.assertEquals("Логин не может быть пустым или содержать пробелы.", exception.getMessage());
    }

    @Test
    void validationBlankLogin() {
        userOne.setLogin("");
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.validateUserData(userOne);
        });
        Assertions.assertEquals("Логин не может быть пустым или содержать пробелы.", exception.getMessage());
    }

    @Test
    void validationBirthday() {
        userOne.setBirthday(LocalDate.of(2030, 10, 10));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.validateUserData(userOne);
        });
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void validationNullName() {
        userOne.setName(null);
        userManager.validateUserData(userOne);
        Assertions.assertEquals(userOne.getLogin(), userOne.getName(), "Пустое имя пользователя не заменилось на логин.");
    }

    @Test
    public void putNewUserInMap() {
        userManager.putNewUserInMap(userOne);
        boolean containsKeyInMap = userManager.getAllUsers().contains(userOne);
        Assertions.assertTrue(containsKeyInMap, "Пользователь, которого нужно было добавить, отсутствует в списке пользователей.");
    }

    @Test
    public void getListUsers() {
        userManager.putNewUserInMap(userOne);
        User userTwo = new User("ant2@", "ant2", "Ant2", LocalDate.of(1900, 1, 2));
        userManager.putNewUserInMap(userTwo);
        List<User> allUsers = userManager.getAllUsers();
        boolean empty = allUsers.isEmpty();
        int allUsersSize = allUsers.size();
        assertFalse(empty, "После добавления двух пользователей список остался пустой.");
        assertEquals(2, allUsersSize, "Число добавленных пользователей и выводимых в списке не совпадают.");
    }

    @Test
    public void getListUsersNull() {
        List<User> allUsers = userManager.getAllUsers();
        assertTrue(allUsers.isEmpty(), "Ложный результат, список пользователей должен быть пуст.");
    }

    @Test
    public void updateMissingUser() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.updateUser(userOne);
        });
        Assertions.assertEquals("Пользователь, данные которого необходимо обновить, отсутствует.", exception.getMessage());
    }

}