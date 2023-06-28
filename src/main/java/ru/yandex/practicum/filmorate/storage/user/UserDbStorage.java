package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserDataConflictsException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Primary
@Data
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addNewUser(User user) {
        String userLogin = user.getLogin();
        log.info("Добавление в БД нового пользователя {}", userLogin);
        String insertUser =
                "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int numberModifiedRows = jdbcTemplate.update((Connection connection) -> {
            PreparedStatement stmt = connection.prepareStatement(insertUser, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, userLogin);
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        if (numberModifiedRows > 0) {
            int userGeneratedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            user.setId(userGeneratedId);
            log.info("В БД добавлен новый пользователь {}, id = {}", userLogin, user.getId());
            return user;
        } else {
            throw new UserDataConflictsException(String.format("При сохранении в БД" +
                " нового пользователя с login = %s возникла ошибка!", userLogin));
        }
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        String userLogin = user.getLogin();
        log.info("Обновление в БД пользователя {}, id = {}", userLogin, userId);
        String updateUser =
                "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE id = ?";

        int numberModifiedRows = jdbcTemplate.update(updateUser,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (numberModifiedRows >= 1) {
            log.info("В БД обновлён пользователь {}, id = {}", userLogin, userId);
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s, которого необходимо обновить," +
                    " отсутствует в БД.", userId));
        }
    }

    @Override
    public User findUserById(int id) {
        log.info("Поиск в БД пользователя с id = {}", id);
        String selectUser =
                "SELECT id, email, login, name, birthday " +
                "FROM users " +
                "WHERE id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(selectUser, id);
        if (userRows.next()) {
            User user = mapRowUser(userRows);
            log.info("В БД найден пользователь {}, id = {}", user.getLogin(), user.getId());
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s в БД не найден.", id));
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Поиск в БД всех пользователей");
        String selectUsers =
                "SELECT id, email, login, name, birthday " +
                "FROM users";
        List<User> usersList = new ArrayList<>();

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(selectUsers);
        while (userRows.next()) {
            usersList.add(mapRowUser(userRows));
        }
        log.info("В базе данных найдено {} пользователей", usersList.size());
        return usersList;
    }

    @Override
    public void deleteUser(int id) {
        log.info("Удаление из базы данных пользователя с id = {}", id);
        String deleteUsers =
                "DELETE FROM users " +
                "WHERE id = ?";

        int numberModifiedRows = jdbcTemplate.update(deleteUsers, id);
        if (numberModifiedRows > 0) {
            log.info("Из базы данных удалён пользователь с id = {}", id);
        } else {
            throw new UserNotFoundException(String.format("Пользователь с id = %s, которого необходимо удалить," +
                    " отсутствует в БД.", id));
        }
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