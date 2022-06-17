package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmDbService {
    FilmStorage filmStorage;

    public FilmDbService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(int id) {
        return filmStorage.get(id);
    }

    public List<Film> getBestFilms(int count) {
        return filmStorage.getBestFilms(count);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }

    public void putLike(int filmId, int userId) {
        if (filmId < 1 || userId < 1) {
            throw new FilmNotFoundException("user or film not found");
        }
        filmStorage.putLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (filmId < 1 || userId < 1) {
            throw new FilmNotFoundException("user or film not found");
        }
        filmStorage.deleteLike(filmId, userId);
    }
}
