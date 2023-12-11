package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @Override
    public Collection<User> getAll() {
        log.info("Получен запрос к эндпоинту /users для получения данных всех пользователей");
        return users.values();
    }

    @Override
    public User create(User user) {
        User userNew = userValidation(user);
        userId += 1;
        userNew.setId(userId);
        userNew.setFilmsLiked(new HashSet<>());
        userNew.setFriends(new HashSet<>());
        users.put(userId, userNew);
        log.info("Получен запрос к эндпоинту /users для добавления нового пользователя");
        return userNew;
    }

    @Override
    public User updateOrCreate(User user) {
        User userUpdated = userValidation(user);
        User userNew = users.get(user.getId());
        if (userNew == null) {
            throw new NullPointerException("Данного пользователя не существует.");
        }
        users.remove(userNew.getId());
        userUpdated.setFilmsLiked(new HashSet<>());
        userUpdated.setFriends(new HashSet<>());
        users.put(userUpdated.getId(), userUpdated);
        log.info("Получен запрос к эндпоинту /users для обновления данных пользователя");
        return user;
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
        log.info("Получен запрос к эндпоинту /users для обновления данных пользователей по ID");
        return response;
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
