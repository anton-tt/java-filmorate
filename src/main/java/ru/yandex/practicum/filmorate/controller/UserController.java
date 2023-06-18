package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping
    public User createNewUser(@RequestBody User user) {
        UserValidation.validateUserData(user);
        log.info("Добавлен новый пользователь: {}", user);
        return userStorage.putNewUserInMap(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        UserValidation.validateUserData(user);
        log.info("Обновлены данные пользователя: {}", user);
        return userStorage.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = userStorage.getAllUsers();
        log.info("Текущее количество пользователей: {}", usersList.size());
        return usersList;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userStorage.deleteUser(id);
        log.info("Пользователь c id {} удалён", id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получены данные пользователя с id=: {}", id);
        return userStorage.getRequiredUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id=: {} добавил в список друзей пользователя с id=: {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь с id=: {} удалил из списка друзей пользователя с id=: {}", id, friendId);
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        List<User> allFriendList = userService.getAllFriendsList(id);
        log.info("Количество друзей у пользователя с id= {}  составляет: {}", id, allFriendList.size());
        return allFriendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getSharedFriendList(@PathVariable int id, @PathVariable int otherId) {
        List<User> sharedFriendList = userService.getSharedFriendList(id, otherId);
        log.info("Количество общих друзей у пользователей с id {} и {} составляет: {}", id, otherId, sharedFriendList.size());
        return sharedFriendList;
    }

}