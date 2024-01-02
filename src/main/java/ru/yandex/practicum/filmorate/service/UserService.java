package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.exception.UnauthorizedCommand;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            throw new FilmOrUserNotRegistered("Пользователь не найден");
        }
        if (user.getFriends().contains(friend.getId())) {
            throw new UnauthorizedCommand("Данные пользователи уже добавили друг друга в друзья.");
        }
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(int id, int friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        if (user == null || friend == null) {
            throw new FilmOrUserNotRegistered("Пользователь не найден");
        }
        if (!user.getFriends().contains(friend.getId())) {
            throw new UnauthorizedCommand("Данные пользователи еще не добавили друг друга в друзья.");
        }
        userStorage.deleteFriend(user, friend);
    }

    public List<User> getFriendsList(int id) {
        User user = userStorage.getUsers().get(id);
        return userStorage.getUsersById(user.getFriends());
    }

    public List<User> getMutualFriends(int id, int otherId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(otherId);
        Set<Integer> userCopy = new HashSet<>(user.getFriends());
        userCopy.retainAll(friend.getFriends());
        return userStorage.getUsersById(userCopy);
    }

    public Collection<User> getAll() {
        return userStorage.getUsers().values();
    }

    public User findById(int id) {
        User user = userStorage.getUsers().get(id);
        if (user == null) {
            throw new FilmOrUserNotRegistered("Пользователь с таким id не " +
                    "зарегистрирован");
        }
        return user;
    }

    public User create(User user) {
        User userNew = userValidation(user);
        return userStorage.create(userNew);
    }

    public User update(User user) {
        User userUpdated = userValidation(user);
        return userStorage.update(userUpdated);
    }

    private User userValidation(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может содержать пробелы.");
            throw new ValidationException("Логин пользователя не может содержать пробелы.");
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.error("День рождения пользователя не может быть в будущем.");
            throw new ValidationException("День рождения пользователя не может быть в будущем.");
        }
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
