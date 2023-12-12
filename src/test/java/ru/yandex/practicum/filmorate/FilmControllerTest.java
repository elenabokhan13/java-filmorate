
package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.model.Film.FORMATTER;

@SpringBootTest
class FilmControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private FilmController filmController;

    @Test
    void filmNullNameValidation() {
        Film film = Film.builder()
                .description("New film")
                .duration(56)
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Название не может быть пустым"));
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

        violations.forEach(action -> assertThat(action.getMessage()).isEqualTo("Название не может быть пустым"));
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

    @Test
    void filmReleaseDateNull() {
        Film film = Film.builder()
                .description("New film")
                .name("Name")
                .id(3)
                .build();
        assertNotNull(filmController.create(film));
    }

    @Test
    void filmDescriptionNull() {
        Film film = Film.builder()
                .name("Name")
                .releaseDate(LocalDate.parse("2002-09-13", FORMATTER))
                .id(3)
                .build();
        assertNotNull(filmController.create(film));
    }
}

