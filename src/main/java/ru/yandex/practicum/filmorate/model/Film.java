package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
   private int id;

   @NotBlank
   private  String name;

   @Size(max = 200)
   private  String description;

   private LocalDate releaseDate;

   private Duration duration;

   private Set<Integer> likes;

   private Set<Genre> genres;

   private MPAA rating;



   public Film(int id, String name, String description, LocalDate releaseDate, Duration duration, MPAA MPAA) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.releaseDate = releaseDate;
      this.duration = duration;
      this.likes = new HashSet<>();
      this.genres = new HashSet<>();
      this.rating = MPAA;
   }
}
