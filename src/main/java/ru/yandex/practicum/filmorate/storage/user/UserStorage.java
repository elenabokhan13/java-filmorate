package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface UserStorage {
    User create(User user);

    User update(User user);

    Map<Integer, User> getUsers();

    List<User> getUsersById(Set<Integer> usersList);

    void addFriend(User user, User friend);

    void deleteFriend(User user, User friend);
}
