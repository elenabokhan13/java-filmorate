package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) throws ValidationException {
        User userNew = userValidation(user);
        userId += 1;
        userNew.setId(userId);
        users.put(userId, userNew);
        log.info("Получен запрос к эндпоинту /users для добавления нового пользователя");
        return userNew;
    }

    @PutMapping
    public User updateOrCreate(@RequestBody @Valid User user) throws ValidationException {
        User userUpdated = userValidation(user);
        User userNew = users.get(user.getId());
        if (userNew == null) {
            throw new ValidationException("Данного пользователя не существует.");
        }
        users.remove(userNew.getId());
        users.put(userUpdated.getId(), userUpdated);
        log.info("Получен запрос к эндпоинту /users для обновления данных пользователя");
        return user;
    }

    private User userValidation(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может содержать пробелы.");
            throw new ValidationException("Логин пользователя не может содержать пробелы.");
        } else if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("День рождения пользователя не может быть в будущем.");
                throw new ValidationException("День рождения пользователя не может быть в будущем.");
            }
        }
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
