package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsersMap();

    User putNewUserInMap(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    void deleteUser(int userId);

    User getRequiredUser(int id);

}