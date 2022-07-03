package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.TreeSet;

public interface DirectorFilmStorage {
    void addDirectorToFilm(int filmId, TreeSet<Director> directors);
    void removeDirectorFromFilm(int filmId);
}
