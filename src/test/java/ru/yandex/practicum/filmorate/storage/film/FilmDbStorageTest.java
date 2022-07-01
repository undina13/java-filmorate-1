package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void getBestFilms() {
        List<Film> filmList = filmDbStorage.getBestFilms(10, null, null);

        assertEquals(filmList.size(), 4, "Количество обзоров не совпало");

        filmList = filmDbStorage.getBestFilms(1, null, null);

        assertEquals(filmList.size(), 1, "Количество обзоров не совпало");

        filmList = filmDbStorage.getBestFilms(10, null, 2010);

        assertEquals(filmList.size(), 0, "Количество обзоров не совпало");

        filmList = filmDbStorage.getBestFilms(10, null, 2022);

        assertEquals(filmList.size(), 3, "Количество обзоров не совпало");

        filmList = filmDbStorage.getBestFilms(10, 5, 22);

        assertEquals(filmList.size(), 0, "Количество обзоров не совпало");
    }
}