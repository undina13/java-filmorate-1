package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorService.addDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping({"/{id}"})
    public Director getDirector(@Positive @PathVariable int id) {
        return directorService.getDirector(id);
    }

    @DeleteMapping({"/{id}"})
    public boolean removeDirector(@Positive @PathVariable int id) {
        return directorService.removeDirector(id);
    }


/**
 1. GET /films/director/{directorId}?sortBy=[year,likes]
 Возвращает список фильмов режиссера отсортированных по количеству лайков или году выпуска.

 2. POST /films
 {
 "name": "New film",
 "releaseDate": "1999-04-30",
 "description": "New film about friends",
 "duration": 120,
 "mpa": { "id": 3},
 "genres": [{ "id": 1}],
 "director": [{ "id": 1}]

 3. GET /directors` - Список всех режиссёров

 4. GET /directors/{id}`- Получение режиссёра по id

 5. POST /directors` - Создание режиссёра

 6. PUT /directors` - Изменение режиссёра
 {
 "id": 1,
 "name": "New director"
 }

 7. DELETE /directors/{id} - Удаление режиссёра
 */
}


