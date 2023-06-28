package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDbStorageTests {
    private final FriendshipDbStorage friendshipDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void addFriendTest() {
        User dataOneUser  = User.builder()
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User oneUser = userDbStorage.addNewUser(dataOneUser);
        int oneUserId = oneUser.getId();

        User dataTwoUser = User.builder()
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User twoUser = userDbStorage.addNewUser(dataTwoUser);
        int twoUserId = twoUser.getId();

        friendshipDbStorage.addFriend(oneUserId, twoUserId);
        List<User> allFriendsList = friendshipDbStorage.getAllFriendsList(oneUserId);

        assertThat(allFriendsList.size()).isEqualTo(1);
    }

    @Test
    public void deleteFriendTest() {
        User dataOneUser  = User.builder()
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User oneUser = userDbStorage.addNewUser(dataOneUser);
        int oneUserId = oneUser.getId();

        User dataTwoUser = User.builder()
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User twoUser = userDbStorage.addNewUser(dataTwoUser);
        int twoUserId = twoUser.getId();

        friendshipDbStorage.addFriend(oneUserId, twoUserId);
        friendshipDbStorage.deleteFriend(oneUserId, twoUserId);
        List<User> allFriendsList = friendshipDbStorage.getAllFriendsList(oneUserId);

        assertThat(allFriendsList.size()).isEqualTo(0);
    }

    @Test
    public void getCommonFriendListTest() {
        User dataOneUser  = User.builder()
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User oneUser = userDbStorage.addNewUser(dataOneUser);
        int oneUserId = oneUser.getId();

        User dataTwoUser = User.builder()
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User twoUser = userDbStorage.addNewUser(dataTwoUser);
        int twoUserId = twoUser.getId();

        User dataThreeUser = User.builder()
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User threeUser = userDbStorage.addNewUser(dataThreeUser);
        int threeUserId = threeUser.getId();

        friendshipDbStorage.addFriend(oneUserId, twoUserId);
        friendshipDbStorage.addFriend(threeUserId, twoUserId);
        List<User> commonFriendsList = friendshipDbStorage.getCommonFriendList(oneUserId, threeUserId);
        int commonFriendId = commonFriendsList.get(0).getId();

        assertThat(commonFriendsList.size()).isEqualTo(1);
        assertThat(commonFriendId).isEqualTo(twoUserId);
    }

}