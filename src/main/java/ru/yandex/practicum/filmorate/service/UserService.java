package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        if (user.getFriends().contains((long) friend.getId())) {
            throw new RuntimeException("Данные пользователи уже добавили друг друга в друзья.");
        }
        Set<Long> currentUser = user.getFriends();
        currentUser.add((long) friend.getId());
        user.setFriends(currentUser);
        Set<Long> currentFriend = friend.getFriends();
        currentFriend.add((long) user.getId());
        friend.setFriends(currentFriend);
    }

    public void deleteFriend(int id, int friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        if (!user.getFriends().contains((long) friend.getId())) {
            throw new RuntimeException("Данные пользователи еще не добавили друг друга в друзья.");
        }
        Set<Long> currentUser = user.getFriends();
        currentUser.remove((long) friend.getId());
        user.setFriends(currentUser);
        Set<Long> currentFriend = friend.getFriends();
        currentFriend.remove((long) user.getId());
        friend.setFriends(currentFriend);
    }

    public List<User> getFriendsList(int id) {
        User user = userStorage.getUsers().get(id);
        return userStorage.getUsersById(user.getFriends());
    }

    public List<User> getMutualFriends(int id, int otherId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(otherId);
        Set<Long> userCopy = new HashSet<Long>(user.getFriends());
        userCopy.retainAll(friend.getFriends());
        return userStorage.getUsersById(userCopy);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User findById(int id) {
        return userStorage.getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow(NullPointerException::new);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }
}
