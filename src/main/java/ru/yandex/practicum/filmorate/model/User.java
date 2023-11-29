package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;

    @Email
    @NotBlank
    @NotEmpty
    private String email;

    @NotBlank
    @NotEmpty
    private String login;
    private String name;
    private LocalDate birthday;
}
