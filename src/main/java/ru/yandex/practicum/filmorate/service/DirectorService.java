package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }


    public Director addDirector(Director director) {
        return directorStorage.addDirector(director);
    }

    public Director updateDirector(Director director) {
        getDirector(director.getId());
        return directorStorage.updateDirector(director);
    }

    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirector(int id) {
        return directorStorage.getDirector(id)
                .orElseThrow(() ->
                        new FilmNotFoundException("Does not contain a director with this id or the id is invalid: " + id));
    }

    public boolean removeDirector(int id) {
        getDirector(id);
        return directorStorage.removeDirector(id);
    }


}
