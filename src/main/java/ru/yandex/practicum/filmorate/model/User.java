package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    private Set<Long> friends;
    private Set<Long> filmsLiked;

    @Email(message = "Введите верный имейл")
    @NotBlank(message = "Введите верный имейл")
    @NotEmpty(message = "Введите верный имейл")
    private String email;

    @NotBlank(message = "Введите логин без пробелов")
    @NotEmpty(message = "Введите логин без пробелов")
    private String login;

    private String name;
    private LocalDate birthday;
}
