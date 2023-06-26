package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void addFriend(int userOneId, int userTwoId);

    void deleteFriend(int userId, int friendId);

    List<User> getAllFriendsList(int userId);

    List<User> getCommonFriendList(int userOneId, int userTwoId);

}