package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Data
@Builder
public class IdNameSet {
    private Integer id;

    @Column(length = 14)
    private String name;
}
