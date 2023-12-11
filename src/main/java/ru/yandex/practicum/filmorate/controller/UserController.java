package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping(value = "/{id}")
    public User findById(@PathVariable int id) {
        return userStorage.getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow(NullPointerException::new);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        User user = userStorage.getUsers().get(id);
        return userStorage.getUsersById(userService.getFriendsList(user));
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(otherId);
        return userStorage.getUsersById(userService.getMutualFriends(user, friend));
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.create(user);
    }

    @PutMapping
    public User updateOrCreate(@RequestBody @Valid User user) throws ValidationException {
        return userStorage.updateOrCreate(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        userService.addFriend(user, friend);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        userService.deleteFriend(user, friend);
    }
}
