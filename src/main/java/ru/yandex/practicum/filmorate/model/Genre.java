package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.Getter;

@Data
public class Genre {
  int id;
  String name;

 public Genre(int id, String name) {
  this.id = id;
  this.name = name;
 }
}
