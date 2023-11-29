package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
public class UserController {
    private final List<User> users = new ArrayList<>();
    private int userId = 0;

    @GetMapping("/users")
    public List<User> getAll() {
        return users;
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        User userNew = userValidation(user);
        userId += 1;
        userNew.setId(userId);
        users.add(userNew);
        log.info("Получен запрос к эндпоинту /users для добавления нового пользователя");

        return userNew;
    }

    @PutMapping("/users")
    public User updateOrCreate(@Valid @RequestBody User user) throws ValidationException {
        User userUpdated = userValidation(user);
        User userNew = null;
        for (User userCurrent : users) {
            if (userCurrent.getId() == userUpdated.getId()) {
                userNew = userCurrent;
                break;
            }
        }
        if (userNew != null) {
            users.remove(userNew);
            users.add(userUpdated);
        } else {
            throw new ValidationException();
        }

        log.info("Получен запрос к эндпоинту /users для обновления данных пользователя");
        return user;
    }

    private User userValidation(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может содержать пробелы.");
            throw new ValidationException();
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("День рождения пользователя не может быть в будущем.");
            throw new ValidationException();
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
