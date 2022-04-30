package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmsLikesTest {
    @Autowired
    FilmController filmService;
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;
    @Autowired
    UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
     filmStorage.put(FilmUserTestData.film1);
        filmStorage.put(FilmUserTestData.film2);
        filmStorage.put(FilmUserTestData.film3);
        filmStorage.put(FilmUserTestData.film4);
        filmStorage.put(FilmUserTestData.film5);
        filmStorage.put(FilmUserTestData.film6);
        filmStorage.put(FilmUserTestData.film7);
        filmStorage.put(FilmUserTestData.film8);
        filmStorage.put(FilmUserTestData.film9);
        filmStorage.put(FilmUserTestData.film10);
        filmStorage.put(FilmUserTestData.film11);

        userStorage.put(FilmUserTestData.user1);
        userStorage.put(FilmUserTestData.user2);
        userStorage.put(FilmUserTestData.user3);
        userStorage.put(FilmUserTestData.user4);
        userStorage.put(FilmUserTestData.user5);
        userStorage.put(FilmUserTestData.user6);
        userStorage.put(FilmUserTestData.user7);
        userStorage.put(FilmUserTestData.user8);
        userStorage.put(FilmUserTestData.user9);
        userStorage.put(FilmUserTestData.user10);
    }

@Test
public void putLikeAllOk() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.put("/films/1/like/1"))
            .andExpect(status().isOk());
    Assertions.assertEquals(filmService.getFilmById(1).getLikes(), Set.of(1));
}

    @Test
    public void putLikeBadFilm() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/15/like/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putLikeBadUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/1/like/13"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteLikeAllOk() throws Exception {
        filmService.putLike(1,1);
        filmService.putLike(1,2);
        Assertions.assertEquals(filmService.getFilmById(1).getLikes(), Set.of(1, 2));
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/1/like/1"))
                .andExpect(status().isOk());
        Assertions.assertEquals(filmService.getFilmById(1).getLikes(), Set.of(2));
    }

    @Test
    public void deleteLikeBadFilm() throws Exception {
        filmService.putLike(1,1);
        filmService.putLike(1,2);
        Assertions.assertEquals(filmService.getFilmById(1).getLikes(), Set.of(1, 2));
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/16/like/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteLikeBadUser() throws Exception {
        filmService.putLike(1, 1);
        filmService.putLike(1, 2);
        Assertions.assertEquals(filmService.getFilmById(1).getLikes(), Set.of(1, 2));
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/1/like/13"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPopular() throws Exception {
        filmService.putLike(1, 1);
        filmService.putLike(2, 2);
        filmService.putLike(3, 3);
        filmService.putLike(4, 4);
        filmService.putLike(5, 5);
        filmService.putLike(6, 6);
        filmService.putLike(7, 7);
        filmService.putLike(8, 8);
        filmService.putLike(9, 9);
        filmService.putLike(10, 10);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/films/popular?count=5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(""));

    }

    @AfterEach
    void delete() {
        filmStorage.deleteAll();
        userStorage.deleteAll();
    }
}
