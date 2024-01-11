package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Jacksonized
public class User {
    @Column(length = 12)
    public static final String FRIENDSHIP = "REQUEST SENT";
    private int id;

    @Builder.Default
    private Set<Integer> friends = new HashSet<>();

    @Builder.Default
    private Set<Integer> filmsLiked = new HashSet<>();

    @Email(message = "Введите верный имейл")
    @NotBlank(message = "Введите верный имейл")
    @NotEmpty(message = "Введите верный имейл")
    @Column(length = 50)
    private String email;

    @NotBlank(message = "Введите логин без пробелов")
    @NotEmpty(message = "Введите логин без пробелов")
    @Column(length = 25)
    private String login;

    @Column(length = 50)
    private String name;

    private LocalDate birthday;
}
