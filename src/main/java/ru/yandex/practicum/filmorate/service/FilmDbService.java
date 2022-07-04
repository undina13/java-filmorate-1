package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.MarksStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmDbService {
    private FilmStorage filmStorage;
    private MarksStorage marksStorage;
    private DirectorService directorService;

    @Autowired
    public FilmDbService
            (@Qualifier("filmDbStorage") FilmStorage filmStorage,
             MarksStorage marksStorage,
             DirectorService directorService) {
        this.filmStorage = filmStorage;
        this.marksStorage = marksStorage;
        this.directorService = directorService;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film get(int id) {
        return filmStorage.get(id);
    }

    public List<Film> getBestFilms(int count, Integer genreId, Integer year) {
        return filmStorage.getBestFilms(count, genreId, year);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film put(Film film) {
        return filmStorage.put(film);
    }

 //TODO    //    public void putLike(int filmId, int userId) {
//        if (filmId < 1 || userId < 1) {
//            throw new FilmNotFoundException("user or film not found");
//        }
//        likeStorage.putLike(filmId, userId);
//    }
// TODO
//    public void deleteLike(int filmId, int userId) {
//        if (filmId < 1 || userId < 1) {
//            throw new FilmNotFoundException("user or film not found");
//        }
//        likeStorage.deleteLike(filmId, userId);
//    }
//TODO
//    public List<Film> getAllFilmsOfDirectorSortedByLikesOrYears(int id, String sortBy) {
//        directorService.getDirector(id);
//        if (sortBy.equals("likes")) {
//            return filmStorage.getAllFilmsOfDirectorSortedByLikes(id);
//        }
//        return filmStorage.getAllFilmsOfDirectorSortedByYears(id);
//    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public Collection<Film> search(String query, List<String> by) {
        return filmStorage.search(query, by);
    }

    public void deleteFilm(int id) {
        filmStorage.deleteFilm(id);
    }
}
