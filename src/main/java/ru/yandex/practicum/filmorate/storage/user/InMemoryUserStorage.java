package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private static int userId = 0;

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
            throw new ObjectNotFound("Пользователь с id " + user.getId() + " не зарегистрирован");
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
    public List<User> getUsersById(Set<Integer> usersList) {
        List<User> response = new ArrayList<>();
        if (usersList.isEmpty()) {
            return response;
        }
        for (Integer id : usersList) {
            response.add(users.get(id));
        }
        return response;
    }

    @Override
    public void addFriend(User user, User friend) {
        Set<Integer> currentUser = user.getFriends();
        currentUser.add(friend.getId());
        user.setFriends(currentUser);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        Set<Integer> currentUser = user.getFriends();
        currentUser.remove(friend.getId());
        user.setFriends(currentUser);
    }
}
