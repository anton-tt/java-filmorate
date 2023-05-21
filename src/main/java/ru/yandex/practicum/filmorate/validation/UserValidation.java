package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidation {
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
