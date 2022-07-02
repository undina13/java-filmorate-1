package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface DirectorStorage {

    Director addDirector(Director director);  // создание режиссера

    Optional<Director> getDirector(int id);   //Получение режиссёра по id

    List<Director> getAllDirectors();         //Список всех режиссёров

    Director updateDirector(Director director);

    boolean removeDirector(int id);
}
