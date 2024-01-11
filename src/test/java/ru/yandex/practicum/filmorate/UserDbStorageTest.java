package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("/schema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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

    @Test
    public void testFindUserById() {
        User savedUser = userStorage.getUsers().values().stream()
                .filter(x -> x.getId() == 1)
                .findFirst().orElseThrow(() -> new ObjectNotFound("Пользователь с таким id не " +
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