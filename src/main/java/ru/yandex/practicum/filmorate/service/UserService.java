package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public User addNewUser(User newUser) {
        UserValidation.validateUserData(newUser);
        User user = userStorage.addNewUser(newUser);
        return user;
    }

    public User updateUser(User updatedUser) {
        UserValidation.validateUserData(updatedUser);
        User user = userStorage.updateUser(updatedUser);
        return user;
    }

    public User findUserById(int id) {
        User user = userStorage.findUserById(id);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = userStorage.getAllUsers();
        return userList;
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public User addFriend(int userOneId, int userTwoId) {
        if (userTwoId > 0) {
            friendshipStorage.addFriend(userOneId, userTwoId);
            User userOne = userStorage.findUserById(userOneId);
            return userOne;
        } else {
            throw new UserNotFoundException(String.format("Пользователя с id = %s нельзя добавить в друзья" +
                    " пользователю с id = %s, т.к. он имеет некорректный id.", userTwoId, userOneId));
        }
    }

    public User deleteFriend(int userOneId, int userTwoId) {
        if (userTwoId > 0) {
            friendshipStorage.deleteFriend(userOneId, userTwoId);
            User userOne = userStorage.findUserById(userOneId);
        return userOne;
        } else {
            throw new UserNotFoundException(String.format("Пользователя с id = %s нельзя удалить из друзей" +
                " пользователя с id = %s, т.к. он имеет некорректный id.", userTwoId, userOneId));
        }
    }

    public List<User> getAllFriendsList(int userId) {
        return friendshipStorage.getAllFriendsList(userId);
    }

    public List<User> getCommonFriendList(int userOneId, int userTwoId) {
        return friendshipStorage.getCommonFriendList(userOneId, userTwoId);
    }

}