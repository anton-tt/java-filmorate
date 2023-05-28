package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage  implements UserStorage {
    private int nextUserId = 1;
    private final Map<Integer, User> usersMap = new HashMap<>();

    private int getNextId() {
        return nextUserId++;
    }

    @Override
    public Map<Integer, User> getUsersMap() {
        return usersMap;
    }

    @Override
    public User putNewUserInMap(User user) {
        user.setId(getNextId());
        usersMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        if (usersMap.containsKey(userId)) {
            usersMap.remove(userId);
            user.setId(userId);
            usersMap.put(userId, user);
            return user;
        } else {
            throw new UserNotFoundException("Пользователь, данные которого необходимо обновить, отсутствует.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        if (!usersMap.isEmpty()) {
            for (Map.Entry<Integer, User> entry : usersMap.entrySet()) {
                usersList.add(entry.getValue());
            }
        }
        return usersList;
    }

    @Override
    public void deleteUser(int userId) {
        if (usersMap.containsKey(userId)) {
            usersMap.remove(userId);
        } else {
            throw new UserNotFoundException("Пользователь, данные которого необходимо удалить, отсутствует.");
        }
    }

    @Override
    public User getRequiredUser(int id) {
        UserValidation.validateUserId(id, usersMap);
        return usersMap.get(id);
    }

}