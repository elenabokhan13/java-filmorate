package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;


@Service
@Slf4j
public class UserService {
    public void addFriend(User user, User friend) {
        if (user.getFriends().contains((long) friend.getId())) {
            throw new RuntimeException("Данные пользователи уже добавили друг друга в друзья.");
        } else {
            Set<Long> currentUser = user.getFriends();
            currentUser.add((long) friend.getId());
            user.setFriends(currentUser);
            Set<Long> currentFriend = friend.getFriends();
            currentFriend.add((long) user.getId());
            friend.setFriends(currentFriend);
            log.info("Получен запрос к эндпоинту /users для добавления друга");
        }
    }

    public void deleteFriend(User user, User friend) {
        if (!user.getFriends().contains((long) friend.getId())) {
            throw new RuntimeException("Данные пользователи еще не добавили друг друга в друзья.");
        } else {
            Set<Long> currentUser = user.getFriends();
            currentUser.remove((long) friend.getId());
            user.setFriends(currentUser);
            Set<Long> currentFriend = friend.getFriends();
            currentFriend.remove((long) user.getId());
            friend.setFriends(currentFriend);
            log.info("Получен запрос к эндпоинту /users для удаления друга");
        }
    }

    public Set<Long> getFriendsList(User user) {
        log.info("Получен запрос к эндпоинту /users для дполучения списка друзей");
        return user.getFriends();
    }

    public Set<Long> getMutualFriends(User user, User friend) {
        Set<Long> userCopy = new HashSet<Long>(user.getFriends());
        userCopy.retainAll(friend.getFriends());
        log.info("Получен запрос к эндпоинту /users для получения списка общих друзей");
        return userCopy;
    }
}
