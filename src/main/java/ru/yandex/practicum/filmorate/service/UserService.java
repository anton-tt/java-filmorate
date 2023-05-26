package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserDataConflictsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addFriend(int userOneId, int userTwoId) {
        User userOne = userStorage.getRequiredUser(userOneId);
        Set<Integer> userOneFriendsSet = userOne.getFriends();
        if (userOneFriendsSet.contains(userTwoId)) {
            throw new UserDataConflictsException(String.format("Пользователь с id = %s ранее уже был добавлен в друзья.", userTwoId));
        } else {
            User userTwo = userStorage.getRequiredUser(userTwoId);
            Set<Integer> userTwoFriendsSet = userTwo.getFriends();
            userOneFriendsSet.add(userTwoId);
            userTwoFriendsSet.add(userOneId);
        }
        return userOne;
    }

    public User deleteFriend(int userOneId, int userTwoId) {
        User userOne = userStorage.getRequiredUser(userOneId);
        Set<Integer> userOneFriendsSet = userOne.getFriends();
        if (!userOneFriendsSet.contains(userTwoId)) {
            throw new UserDataConflictsException(String.format("Пользователь с id = %s не являлся другом, удалить из друзей невозможно.", userTwoId));
        } else {
            User userTwo = userStorage.getRequiredUser(userTwoId);
            Set<Integer> userTwoFriendsSet = userTwo.getFriends();
            userOneFriendsSet.remove(userTwoId);
            userTwoFriendsSet.remove(userOneId);
        }
        return userOne;
    }

    public List<User> getAllFriendsList(int userId) {
        User user = userStorage.getRequiredUser(userId);
        Set<Integer> friends = user.getFriends();
        List<User> allFriendsList = new ArrayList<>();
        for (int friendUserId : friends) {
            allFriendsList.add(userStorage.getRequiredUser(friendUserId));
        }
        return allFriendsList;
    }

    public List<User> getSharedFriendList(int userOneId, int userTwoId) {
        User userOne = userStorage.getRequiredUser(userOneId);
        Set<Integer> userOneFriendsSet = userOne.getFriends();
        User userTwo = userStorage.getRequiredUser(userTwoId);
        Set<Integer> userTwoFriendsSet = userTwo.getFriends();
        List<User> sharedFriendList = new ArrayList<>();
        for (int friendUserId : userOneFriendsSet) {
            if (userTwoFriendsSet.contains(friendUserId)) {
                sharedFriendList.add(userStorage.getRequiredUser(friendUserId));
            }
        }
        return sharedFriendList;
    }

}