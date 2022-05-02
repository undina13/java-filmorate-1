package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film put(Film film);

    Collection<Film> getAll();

    Film get(int id);

    //for testing
    void deleteAll();
}
