package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void putLike(int filmId, int userId){
        filmStorage.get(filmId).getLikes().add(userStorage.get(userId).getId());
    }

    public void deleteLike(int filmId, int userId){
        filmStorage.get(filmId).getLikes().remove(userStorage.get(userId).getId());
    }

//    public List<Film> getBestFilms(int count){
//        Collection<Film> films =  filmStorage.getAll();
//        List<Film> film1 = films.stream().toList();
//        Comparator<Film> comparator = Comparator.comparingInt(f -> f.getLikes().size());
//        films.sort(comparator);
//        return films.subList(0, count-1);
//
//    }
}
