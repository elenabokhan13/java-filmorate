
package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@SpringBootTest
@ContextConfiguration(classes = {UserController.class, ValidationAutoConfiguration.class})
class FilmorateApplicationTests {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private FilmController filmController = new FilmController();
    private UserController userController = new UserController();

    @Test
    void userNullEmailValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Введите верный имейл");
        });
    }

    @Test
    void userEmptyEmailValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Введите верный имейл");
        });
    }

    @Test
    void userNoEmailSymbolValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("user.yandex")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Введите верный имейл");
        });
    }

    @Test
    void userNullLoginValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .name("User")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Введите логин без пробелов");
        });
    }

    @Test
    void userEmptyLoginValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("")
                .name("User")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Введите логин без пробелов");
        });
    }

    @Test
    void userSlashInLoginValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("User next")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void userBirthdayInFutureValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .birthday(LocalDate.parse("2998-08-12", FORMATTER))
                .login("user")
                .name("User")
                .build();
        assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void filmNullNameValidation() {
        Film film = Film.builder()
                .description("New film")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Название не может быть пустым");
        });
    }

    @Test
    void filmEmptyNameValidation() {
        Film film = Film.builder()
                .description("New film")
                .name("")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        violations.forEach(action -> {
            assertThat(action.getMessage()).isEqualTo("Название не может быть пустым");
        });
    }

    @Test
    void filmLongDescriptionValidation() {
        Film film = Film.builder()
                .description("New film New film New film New filmNew film New filmNew film New filmNew " +
                        "film New filmNew film New filmNew film New filmNew film New filmNew film New filmNew " +
                        "New film New filmNew film New filmNew film New filmfilm New film")
                .name("Name")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmInvalidReleaseDateValidation() {
        Film film = Film.builder()
                .description("New film")
                .name("Name")
                .duration(56)
                .releaseDate(LocalDate.parse("1002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmNegativeDurationValidation() {
        Film film = Film.builder()
                .description("New film")
                .name("Name")
                .duration(-10)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

}

