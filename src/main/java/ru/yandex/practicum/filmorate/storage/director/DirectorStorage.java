package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    Director addDirector(Director director);

    Optional<Director> getDirector(int id);

    List<Director> getAllDirectors();

    Director updateDirector(Director director);

    boolean removeDirector(int id);
}
