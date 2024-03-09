package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createNewUser(@RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user);
        return userService.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновление данных пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получение данные пользователя с id = {}", id);
        return userService.findUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = userService.getAllUsers();
        log.info("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        log.info("Удаление пользователя c id = {}", id);
        userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id = {} добавляет в список друзей пользователя с id = {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id = {} удаляет из списка друзей пользователя с id = {}", id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        log.info("Получение списка друзей пользователя с id = {}", id);
        List<User> allFriendList = userService.getAllFriendsList(id);
        log.info("Количество друзей у пользователя с id = {}  составляет {}", id, allFriendList.size());
        return allFriendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendList(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получение списка общих друзей пользователя с id = {} и пользователя с id = {}", id, otherId);
        List<User> commonFriendList = userService.getCommonFriendList(id, otherId);
        log.info("Количество общих друзей у пользователей с id {} и {} составляет: {}", id, otherId, commonFriendList.size());
        return commonFriendList;
    }

}