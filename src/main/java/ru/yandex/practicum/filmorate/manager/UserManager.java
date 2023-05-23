package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.User;
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

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        if (!usersMap.isEmpty()) {
            for (Map.Entry<Integer, User> entry : usersMap.entrySet()) {
                usersList.add(entry.getValue());
            }
        }
        return usersList;
    }

    public void deleteUser(int userId) {
        if (usersMap.containsKey(userId)) {
            usersMap.remove(userId);
        } else {
            throw new RuntimeException("Пользователь, данные которого необходимо удалить, отсутствует.");
        }
    }

}