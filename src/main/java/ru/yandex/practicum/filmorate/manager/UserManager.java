package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {
    private int nextUserId = 1;
    private final Map<Integer, User> usersMap = new HashMap<>();

    private int getNextId() {
        return nextUserId++;
    }

    public User putNewUserInMap(User user) {
        user.setId(getNextId());
        usersMap.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        int userId = user.getId();
        if (usersMap.containsKey(userId)) {
            usersMap.remove(userId);
            user.setId(userId);
            usersMap.put(userId, user);
            return user;
        } else {
            throw new RuntimeException("Пользователь, данные которого необходимо обновить, отсутствует.");
        }
    }

    public List<Integer> getAllUsers() {
        List<Integer> usersList = new ArrayList<>();
        if (!usersMap.isEmpty()) {
            for (Map.Entry<Integer, User> entry : usersMap.entrySet()) {
                usersList.add(entry.getKey());
            }
        }
        return usersList;
    }

    public void validateUserData(User user) {
        String userMail = user.getEmail();
        String userLogin = user.getLogin();
        String userName = user.getName();
        LocalDate userBirthday = user.getBirthday();
        LocalDate currentDate = LocalDate.now();

        if (userMail == null || userMail.isBlank() || !userMail.contains("@")) {
            throw new ValidationException("Адрес электронной почты введён в неправильном формате.");
        }
        if (userLogin == null || userLogin.isBlank() || userLogin.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        }
        if (userName == null || userName.isBlank()) {
            user.setName(userLogin);
        }
        if (userBirthday.isAfter(currentDate)) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}