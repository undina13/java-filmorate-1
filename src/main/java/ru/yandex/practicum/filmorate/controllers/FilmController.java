package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Getter
public class FilmController {

   FilmDbService filmDbService;
    FilmValidator filmValidator;

    @Autowired
    public FilmController(FilmDbService filmDbService) {
        this.filmDbService = filmDbService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmDbService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id){
        return filmDbService.get(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count){
        return filmDbService.getBestFilms(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
     filmValidator.validate(film);

        log.info("Добавляемый film: {}", film);
        return filmDbService.create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
     filmValidator.validate(film);

        log.info("Изменяемый film: {}", film);
        return filmDbService.put(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable int id, @PathVariable int userId){
        filmDbService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId){
        filmDbService.deleteLike(id, userId);
    }


}
