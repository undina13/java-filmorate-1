package ru.yandex.practicum.filmorate.storage.film;

import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film get(int id) {
        if(!films.containsKey(id)){
            throw new FilmNotFoundException("Film with id = " + id + " not found");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getBestFilms(int count) {
        return null;
    }

    @Override
    public void putLike(int filmId, int userId) {

    }

    @Override
    public void deleteLike(int filmId, int userId) {

    }
}
