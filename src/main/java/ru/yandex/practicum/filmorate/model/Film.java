package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Jacksonized
public class Film {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private int id;
    private int likes;

    @Builder.Default
    private List<IdNameSet> genres = new ArrayList<>();
    private IdNameSet mpa;

    @NotBlank(message = "Название не может быть пустым")
    @NotEmpty(message = "Название не может быть пустым")
    @Column(length = 50)
    private String name;
    @Column(length = 200)
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
