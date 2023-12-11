package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface UserStorage {

    Collection<User> getAll();

    User create(User user);

    User updateOrCreate(User user);

    Map<Integer, User> getUsers();

    List<User> getUsersById(Set<Long> usersList);

}
