package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
public class Film {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int id;
    private int likes;
    private List<Genre> genres;
    private Mpa mpa;

    @NotBlank(message = "Название не может быть пустым")
    @NotEmpty(message = "Название не может быть пустым")
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
