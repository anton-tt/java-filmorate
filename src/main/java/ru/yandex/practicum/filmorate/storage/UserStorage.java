package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {
    User putNewUserInMap(User user);
    User updateUser(User user);
    List<User> getAllUsers();
    void deleteUser(int userId);

}
