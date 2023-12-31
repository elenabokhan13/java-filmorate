package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    private Set<Integer> friends;
    private Set<Integer> filmsLiked;

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
