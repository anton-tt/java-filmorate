package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmDataConflictsException;
import ru.yandex.practicum.filmorate.exception.UserDataConflictsException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Data
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(int userId, int friendId) {
        log.info("Сохранение в БД: пользователь c id = {} добавил в друзья" +
                " пользователя с id = {}.", userId, friendId);
        String insertFriend =
                "INSERT INTO friendship (user_one_id, user_two_id) " +
                        "VALUES (?, ?)";

        int numberModifiedRows = jdbcTemplate.update(insertFriend,
                userId,
                friendId);
        if (numberModifiedRows >= 1) { // >= 1
            log.info("В БД сохранено: пользователь c id = {} добавил в друзья  пользователя с id = {}.", userId, friendId);
        } else {
            throw new UserDataConflictsException(String.format("При сохранении в БД" +
                    " добавления в друзья пользователем с id = %s пользователя с id = %s " +
                    "возникла ошибка!", userId, friendId));
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("Удаление из БД: пользователь c id = {} ранее добавил в друзья" +
                " пользователя с id = {}.", userId, friendId);
        String deleteFriend =
                "DELETE FROM friendship " +
                        "WHERE user_one_id = ? AND user_two_id = ?";
        int numberModifiedRows = jdbcTemplate.update(deleteFriend,
                userId,
                friendId);
        if (numberModifiedRows >= 1) { // >= 1
            log.info("Из БД удалено: пользователь c id = {} добавил в друзья пользователя с id = {}.", userId, friendId);
        } else {
            throw new FilmDataConflictsException(String.format("При удалении из БД" +
                    " добавления в друзья пользователем с id = %s пользователя с id = %s " +
                    "возникла ошибка!", userId, friendId));
        }
    }

    @Override
    public List<User> getAllFriendsList(int userId) {
        log.info("Поиск в БД всех друзей пользователя с id = {}", userId);
        String selectFriends =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE id IN " +
                        "(SELECT user_two_id FROM friendship " +
                        "WHERE user_one_id = ?)";

        List<User> allFriendsList = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(selectFriends, userId);
        while (userRows.next()) {
            allFriendsList.add(mapRowUser(userRows));
        }
        log.info("В базе данных найдено {} друзей", allFriendsList.size());
        return allFriendsList;
    }

    @Override
    public List<User> getCommonFriendList(int userOneId, int userTwoId) {
        log.info("Поиск в БД общих друзей пользователей с id = {} и id = {}", userOneId, userTwoId);
        String selectCommon =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE id IN " +
                            "(SELECT user_two_id " +
                             "FROM friendship " +
                             "WHERE user_one_id = ? AND user_two_id IN " +
                                 "(SELECT user_two_id " +
                                 "FROM friendship " +
                                 "WHERE user_one_id = ?))";
        List<User> commonFriendsList = new ArrayList<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(selectCommon, userOneId, userTwoId);
        while (userRows.next()) {
            commonFriendsList.add(mapRowUser(userRows));
        }
        log.info("В базе данных найдено {} общих друзей " +
                "пользователей с id = {} и id = {}", userOneId, userTwoId, commonFriendsList.size());
        return commonFriendsList;
    }

    private User mapRowUser(SqlRowSet userRows) {
        int id = userRows.getInt("id");
        String email = userRows.getString("email");
        String login = userRows.getString("login");
        String name = userRows.getString("name");
        LocalDate birthday = Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate();


        User user = User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        user.setId(id);
        return user;
    }

}