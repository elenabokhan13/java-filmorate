package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmOrUserNotRegistered;
import ru.yandex.practicum.filmorate.exception.UnauthorizedCommand;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@Service
@Slf4j
public class FilmService {
    public static final LocalDate START_RELEASE_DATE = LocalDate.parse("1895-12-28", FORMATTER);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void likeFilm(int id, int userId) {
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(id);
        if (user == null || film == null) {
            throw new FilmOrUserNotRegistered("Получен незарегистрированный фильм или пользователь");
        }
        if (user.getFilmsLiked().contains((long) film.getId())) {
            throw new UnauthorizedCommand("Пользователь уже залайкал данный фильм. Пользователь может поставить фильму " +
                    "только один лайк.");
        }
        Set<Long> currentUser = user.getFilmsLiked();
        currentUser.add((long) film.getId());
        user.setFilmsLiked(currentUser);
        film.setLikes(film.getLikes() + 1);

    }

    public void dislikeFilm(int id, int userId) {
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(id);
        if (user == null || film == null) {
            throw new FilmOrUserNotRegistered("Получен незарегистрированный фильм или пользователь");
        }
        if (!user.getFilmsLiked().contains((long) film.getId())) {
            throw new UnauthorizedCommand("Пользователь еще не залайкал данный фильм.");
        }
        Set<Long> currentUser = user.getFilmsLiked();
        currentUser.remove((long) film.getId());
        user.setFilmsLiked(currentUser);
        film.setLikes(film.getLikes() - 1);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getTopPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findById(int id) {
        return filmStorage.getAll().stream()
                .filter(x -> x.getId() == id)
                .findFirst().orElseThrow(() -> new FilmOrUserNotRegistered("Фильм с таким id не зарегистрирован"));
    }

    public Film create(Film film) {
        filmValidation(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmValidation(film);
        return filmStorage.update(film);
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.error("Описание фильма не может больше 200 символов");
            throw new ValidationException("Описание фильма не может больше 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.error("Дата релиза фильма не может быть раньше 28.12.1895");
            throw new ValidationException("Дата релиза фильма не может быть раньше START_RELEASE_DATE 28.12.1895");
        }
        if (film.getDuration() < 0) {
            log.error("Длительность фильма не может быть отрицательной");
            throw new ValidationException("Длительность фильма не может быть отрицательной");
        }
        if (StringUtils.isBlank(film.getName())) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }
    }
}
