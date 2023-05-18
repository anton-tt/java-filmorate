package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserManager userManager = new UserManager();

    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {
        userManager.validateUserData(user);
        log.debug("Добавлен новый пользователь: {}", user);
        return userManager.putNewUserInMap(user);
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {
        userManager.validateUserData(user);
        log.debug("Обновлены данные пользователя: {}", user);
        return userManager.updateUser(user);
    }

    @GetMapping
    public List<Integer> getAllUsers() {
        List<Integer> usersList = userManager.getAllUsers();
        log.debug("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

}