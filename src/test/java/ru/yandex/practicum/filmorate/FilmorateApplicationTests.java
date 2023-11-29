package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void userValidation() throws ValidationException {
        UserController controller = new UserController();

        User user1 = User.builder()
                .id(1)
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user1));

        User user2 = User.builder()
                .id(1)
                .email("")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user2));

        User user3 = User.builder()
                .id(1)
                .email("user.yandex")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user3));

        User user4 = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user4));

        User user5 = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user5));

        User user6 = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("User next")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user6));

        User user7 = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2998-08-12", FORMATTER))
                .login("user")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> controller.create(user7));
    }

    @Test
    void filmValidation() {
        FilmController controller = new FilmController();

        Film film1 = Film.builder()
                .description("New film")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> controller.create(film1));

        Film film2 = Film.builder()
                .description("New film")
                .name("")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> controller.create(film2));

        Film film3 = Film.builder()
                .description("New film New film New film New filmNew film New filmNew film New filmNew " +
                        "film New filmNew film New filmNew film New filmNew film New filmNew film New filmNew " +
                        "New film New filmNew film New filmNew film New filmfilm New film")
                .name("Name")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> controller.create(film3));

        Film film4 = Film.builder()
                .description("New film")
                .name("Name")
                .duration(56)
                .releaseDate(LocalDate.parse("1002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> controller.create(film4));

        Film film5 = Film.builder()
                .description("New film")
                .name("Name")
                .duration(-10)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> controller.create(film5));
    }

}
