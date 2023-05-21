package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserManagerTests {
    private UserManager userManager;
    private UserValidation userValidation;

    @BeforeEach
    public void beforeEach() {
        userManager = new UserManager();
        userValidation = new UserValidation();
    }

    @Test
    void testValidationBlankEmail() {
        User userOne = new User("", "ant", LocalDate.of(1900, 1, 1));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userValidation.validateUserData(userOne);
        });
        Assertions.assertEquals("Адрес электронной почты введён в неправильном формате.", exception.getMessage());
    }

    @Test
    void testValidationUnformatEmail() {
        User userOne = new User("ant", "ant", LocalDate.of(1900, 1, 1));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userValidation.validateUserData(userOne);
        });
        Assertions.assertEquals("Адрес электронной почты введён в неправильном формате.", exception.getMessage());
    }

    @Test
    void testValidationNullLogin() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            User userOne = new User("ant@", null, LocalDate.of(1900, 1, 1));
        });
        Assertions.assertEquals("login is marked non-null but is null", exception.getMessage());
    }

    @Test
    void testValidationBlankLogin() {
        User userOne = new User("ant@", "", LocalDate.of(1900, 1, 1));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userValidation.validateUserData(userOne);
        });
        Assertions.assertEquals("Логин не может быть пустым или содержать пробелы.", exception.getMessage());
    }

    @Test
    void testValidationBirthday() {
        User userOne = new User("ant@", "ant", LocalDate.of(2030, 10, 10));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userValidation.validateUserData(userOne);
        });
        Assertions.assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void testValidationBlankName() {
        User userOne = new User("ant@", "ant", LocalDate.of(1900, 1, 1));
        userOne.setName("");
        userValidation.validateUserData(userOne);
        Assertions.assertEquals(userOne.getLogin(), userOne.getName(), "Пустое имя пользователя не заменилось на логин.");
    }

    @Test
    public void testPutNewUserInMap() {
        User userOne = new User("ant@", "ant", LocalDate.of(1900, 1, 1));
        userManager.putNewUserInMap(userOne);
        boolean containsKeyInMap = userManager.getAllUsers().contains(userOne);
        Assertions.assertTrue(containsKeyInMap, "Пользователь, которого нужно было добавить, отсутствует в списке пользователей.");
    }

    @Test
    public void testGetListUsers() {
        User userOne = new User("ant@", "ant", LocalDate.of(1900, 1, 1));
        userManager.putNewUserInMap(userOne);
        User userTwo = new User("ant2@", "ant2", LocalDate.of(1900, 1, 2));
        userManager.putNewUserInMap(userTwo);
        List<User> allUsers = userManager.getAllUsers();
        boolean empty = allUsers.isEmpty();
        int allUsersSize = allUsers.size();
        assertFalse(empty, "После добавления двух пользователей список остался пустой.");
        assertEquals(2, allUsersSize, "Число добавленных пользователей и выводимых в списке не совпадают.");
    }

    @Test
    public void testGetListUsersNull() {
        List<User> allUsers = userManager.getAllUsers();
        assertTrue(allUsers.isEmpty(), "Ложный результат, список пользователей должен быть пуст.");
    }

    @Test
    public void testUpdateMissingUser() {
        User userOne = new User("ant@", "ant", LocalDate.of(1900, 1, 1));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            userManager.updateUser(userOne);
        });
        Assertions.assertEquals("Пользователь, данные которого необходимо обновить, отсутствует.", exception.getMessage());
    }

    @Test
    public void testDeleteUser() {
        User userOne = new User("ant@", "ant", LocalDate.of(1900, 1, 1));
        userManager.putNewUserInMap(userOne);
        userManager.deleteUser(userOne.getId());
        boolean containsKeyInMap = userManager.getAllUsers().contains(userOne);
        Assertions.assertFalse(containsKeyInMap, "Пользователь не был удалён из списка пользователей.");
    }

}