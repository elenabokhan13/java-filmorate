package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class Film {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int id;
    private int likes;

    @NotBlank(message = "Название не может быть пустым")
    @NotEmpty(message = "Название не может быть пустым")
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
