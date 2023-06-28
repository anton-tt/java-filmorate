package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDbStorageTests {
    private final LikesDbStorage likesDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void addFilmLikeTest() {
        Film dataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film film = filmDbStorage.addNewFilm(dataFilm);
        int filmId = film.getId();

        User dataUser = User.builder()
                .id(0)
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User user = userDbStorage.addNewUser(dataUser);
        int userId = user.getId();

        likesDbStorage.addFilmLike(filmId, userId);
        List<Integer> likeList = likesDbStorage.getLikesOneFilm(filmId);

        assertThat(likeList.size()).isEqualTo(1);
    }

    @Test
    public void deleteFilmLikeTest() {
        Film dataFilm = Film.builder()
                .id(0)
                .name("movie")
                .description("moviemovie")
                .releaseDate(LocalDate.of(1950, 1, 1))
                .duration(120)
                .rate(0)
                .mpa(Mpa.builder().id(1).build())
                .build();
        Film film = filmDbStorage.addNewFilm(dataFilm);
        int filmId = film.getId();

        User dataOneUser = User.builder()
                .id(0)
                .email("mail@yandex.ru")
                .login("ant")
                .name("anton")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User oneUser = userDbStorage.addNewUser(dataOneUser);
        int oneUserId = oneUser.getId();
        likesDbStorage.addFilmLike(filmId, oneUserId);

        User dataTwoUser = User.builder()
                .email("mail@mail.ru")
                .login("sts")
                .name("stas")
                .birthday(LocalDate.of(1901, 1, 1))
                .build();
        User twoUser = userDbStorage.addNewUser(dataTwoUser);
        int twoUserId = twoUser.getId();
        likesDbStorage.addFilmLike(filmId, twoUserId);

        likesDbStorage.deleteFilmLike(filmId, oneUserId);
        List<Integer> likeList = likesDbStorage.getLikesOneFilm(filmId);

        assertThat(likeList.size()).isEqualTo(1);
    }

}