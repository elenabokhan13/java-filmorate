package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private User newUserOne;
    private User newUserTwo;
    private UserDbStorage userStorage;

    @BeforeEach
    public void createUsers() {
        newUserOne = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .filmsLiked(Set.of())
                .friends(Set.of())
                .build();

        userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.create(newUserOne);

        newUserTwo = User.builder()
                .id(2)
                .email("user@email.ru")
                .login("updatedvanya123")
                .name("Updated Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .filmsLiked(Set.of())
                .friends(Set.of())
                .build();

        userStorage.create(newUserTwo);
    }

    @AfterEach
    public void deleteUsers() {
        String sqlOne = "drop table users cascade";
        jdbcTemplate.update(sqlOne);

        String sqlTwo = "drop table films cascade";
        jdbcTemplate.update(sqlTwo);

        String sqlThree = "drop table friends_list cascade";
        jdbcTemplate.update(sqlThree);

        String sqlFour = "drop table genre_film cascade";
        jdbcTemplate.update(sqlFour);

        String sqlFive = "drop table films_liked_list cascade";
        jdbcTemplate.update(sqlFive);

        String sqlSix = "" +
                "CREATE TABLE IF NOT EXISTS users (\n" +
                "    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                "    name varchar(50),\n" +
                "    login varchar(25),\n" +
                "    email varchar(50),\n" +
                "    birthday date\n" +
                ");";
        jdbcTemplate.update(sqlSix);

        String sqlSeven = "" +
                "CREATE TABLE IF NOT EXISTS films (\n" +
                "    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,\n" +
                "    name varchar(50),\n" +
                "    rating_id INTEGER REFERENCES rating_list(rating_id) ON DELETE CASCADE,\n" +
                "    description varchar(200),\n" +
                "    release_date date,\n" +
                "    duration integer\n" +
                ");";
        jdbcTemplate.update(sqlSeven);

        String sqlEight = "" +
                "CREATE TABLE IF NOT EXISTS friends_list (\n" +
                "    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,\n" +
                "    friend_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,\n" +
                "    friendship_status varchar(11)\n" +
                ");";
        jdbcTemplate.update(sqlEight);

        String sqlNine = "" +
                "CREATE TABLE IF NOT EXISTS genre_film (\n" +
                "    film_id INTEGER REFERENCES films(film_id) ON DELETE CASCADE,\n" +
                "    genre_id INTEGER REFERENCES genre_list(genre_id) ON DELETE CASCADE\n" +
                ");";
        jdbcTemplate.update(sqlNine);

        String sqlTen = "" +
                "CREATE TABLE IF NOT EXISTS films_liked_list (\n" +
                "    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,\n" +
                "    film_id INTEGER REFERENCES films(film_id) ON DELETE CASCADE\n" +
                ");";
        jdbcTemplate.update(sqlTen);
    }

    @Test
    public void testFindUserById() {
        User savedUser = userStorage.getUsers().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().orElseThrow(() -> new FilmOrUserNotRegistered("Пользователь с таким id не " +
                        "зарегистрирован"));

        assertThat(savedUser).isEqualTo(newUserOne);
    }

    @Test
    public void testCreateUser() {
        User createdUser = userStorage.create(newUserOne);
        assertThat(createdUser).isEqualTo(newUserOne);
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = User.builder()
                .id(1)
                .email("user@email.ru")
                .login("updatedvanya123")
                .name("Updated Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .filmsLiked(Set.of())
                .friends(Set.of())
                .build();

        User updatedUserReturn = userStorage.update(updatedUser);

        assertThat(updatedUserReturn).isEqualTo(updatedUser);
    }

    @Test
    public void testGetUsers() {
        Collection<User> userCollection = new ArrayList<>();
        userCollection.add(newUserOne);
        userCollection.add(newUserTwo);

        Collection<User> userCollectionReturn = userStorage.getUsers().values();

        assertThat(userCollection.toString()).isEqualTo(userCollectionReturn.toString());
    }

    @Test
    public void testGetUsersById() {
        List<User> userList = new ArrayList<>();
        userList.add(newUserOne);
        userList.add(newUserTwo);

        List<User> userListReturn = userStorage.getUsersById(Set.of(1, 2));

        assertThat(userList).isEqualTo(userListReturn);
    }

    @Test
    public void testAddFriend() {
        userStorage.addFriend(newUserOne, newUserTwo);
        User userSaved = userStorage.getUsers().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().get();

        assertThat(userSaved.getFriends()).isEqualTo(Set.of(2));
    }

    @Test
    public void testDeleteFriend() {
        userStorage.addFriend(newUserOne, newUserTwo);
        userStorage.deleteFriend(newUserOne, newUserTwo);

        User userSaved = userStorage.getUsers().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().get();

        assertThat(userSaved.getFriends()).isEqualTo(Set.of());
    }
}