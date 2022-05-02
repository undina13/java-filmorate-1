package ru.yandex.practicum.filmorate.storage.film;

import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

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
    public void deleteAll() {
        films.clear();
    }


}
