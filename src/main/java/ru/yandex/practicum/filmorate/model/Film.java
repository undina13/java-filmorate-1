package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    private int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Set<Mark> marks;

    private Set<Genre> genres;

    private TreeSet<Director> directors = new TreeSet<>();

    private MPAA mpa;

    private double rate;

    public Film(int id, String name, String description, LocalDate releaseDate, Integer duration, MPAA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = 0;
    }

    public void setDirectors(TreeSet<Director> directors) {
        this.directors = directors;
    }
}
