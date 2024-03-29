package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addNewUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    void deleteUser(int userId);

    User findUserById(int id);

}