package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserStorage userStorage = new InMemoryUserStorage();
    UserValidation userValidation = new UserValidation();

    @PostMapping
    public User createNewUser(@RequestBody User user) {
        userValidation.validateUserData(user);
        log.info("Добавлен новый пользователь: {}", user);
        return userStorage.putNewUserInMap(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        userValidation.validateUserData(user);
        log.info("Обновлены данные пользователя: {}", user);
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = userStorage.getAllUsers();
        log.info("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable int userID) {
        userStorage.deleteUser(userID);
        log.info("Пользователь c id {} удалён", userID);
    }

}