
package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@SpringBootTest
class UserControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private UserController userController;

    @Test
    void userNullEmailValidation() throws ValidationException {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.parse("1998-08-12", FORMATTER))
                .login("user_new")
                .name("User")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Введите верный имейл"));
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

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Введите верный имейл"));
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

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Введите верный имейл"));
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

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Введите логин без пробелов"));
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

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Введите логин без пробелов"));
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
}

