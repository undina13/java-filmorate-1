package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film put(Film film);

    Collection<Film> getAll();

    Film get(int id);

    List<Film> getBestFilms(int count, Integer genreId, Integer year);

    List<Film> getAllFilmsOfDirectorSortedByLikes(int id);

    List<Film> getAllFilmsOfDirectorSortedByYears(int id);

    List<Film> getCommonFilms(int userId, int friendId);

    Collection<Film> search(String query, List<String> by);

    void deleteFilm(int id);
}
