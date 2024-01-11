package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен запрос к эндпоинту /users для получения данных всех пользователей");
        return userService.getAll();
    }

    @GetMapping(value = "/{id}")
    public User findById(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту /users для обновления данных пользователей по id " + id);
        return userService.findById(id);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        log.info("Получен запрос к эндпоинту /users для дполучения списка друзей пользователя с id " + id);
        return userService.getFriendsList(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос к эндпоинту /users для получения списка общих друзей пользователя с id "
                + id + " и id " + otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) throws ValidationException {
        log.info("Получен запрос к эндпоинту /users для добавления нового пользователя с id " + user.getId());
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) throws ValidationException {
        log.info("Получен запрос к эндпоинту /users для обновления данных пользователя с id " + user.getId());
        return userService.update(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос к эндпоинту /users для добавления друга с id " + friendId
                + " пользователю с id " + id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос к эндпоинту /users для удаления друга с id " + friendId
                + " у пользователя с id " + id);
        userService.deleteFriend(id, friendId);
    }
}
