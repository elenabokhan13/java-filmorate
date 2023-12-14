package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int userId = 0;

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        userId += 1;
        user.setId(userId);
        user.setFilmsLiked(new HashSet<>());
        user.setFriends(new HashSet<>());
        users.put(userId, user);
        return user;
    }

    @Override
    public User update(User user) {
        User userNew = users.get(user.getId());
        if (userNew == null) {
            throw new FilmOrUserNotRegistered("Данного пользователя не существует.");
        }
        users.remove(userNew.getId());
        user.setFilmsLiked(new HashSet<>());
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public List<User> getUsersById(Set<Long> usersList) {
        List<User> response = new ArrayList<>();
        if (usersList.isEmpty()) {
            return response;
        }
        for (Long id : usersList) {
            response.add(users.get(id.intValue()));
        }
        return response;
    }
}
