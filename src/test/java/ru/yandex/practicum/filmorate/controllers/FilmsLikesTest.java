package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDBService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmsLikesTest {
    @Autowired
    FilmDbService filmDbService;
    @Autowired
    FilmDbStorage filmStorage;
    @Autowired
    UserDbStorage userStorage;
    @Autowired
    UserDBService userService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        filmStorage.put(FilmUserTestData.film1);
        FilmUserTestData.film1.getLikes().clear();
        filmStorage.put(FilmUserTestData.film2);
        FilmUserTestData.film2.getLikes().clear();
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
    @DirtiesContext
    public void putLikeAllOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/1/like/1"))
                .andExpect(status().isOk());
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(1));
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
        filmDbService.putLike(1, 1);
        filmDbService.putLike(1, 2);
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(1, 2));
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/1/like/1"))
                .andExpect(status().isOk());
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(2));
    }

    @Test
    public void deleteLikeBadFilm() throws Exception {
        filmDbService.putLike(1, 1);
        filmDbService.putLike(1, 2);
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(1, 2));
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/16/like/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteLikeBadUser() throws Exception {
        filmDbService.putLike(1, 1);
        filmDbService.putLike(1, 2);
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(1, 2));
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/1/like/13"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPopular() throws Exception {
        filmDbService.putLike(1, 1);
        filmDbService.putLike(1, 2);
        filmDbService.putLike(2, 3);
        filmDbService.putLike(2, 4);
        filmDbService.putLike(2, 5);
        filmDbService.putLike(6, 6);
        filmDbService.putLike(7, 7);
        filmDbService.putLike(8, 8);
        filmDbService.putLike(9, 9);
        filmDbService.putLike(10, 10);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/films/popular?count=5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":2,\"name\":\"New film2\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[3,4,5]},{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[1,2]},{\"id\":6,\"name\":\"New film6\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[6]},{\"id\":7,\"name\":\"New film7\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[7]},{\"id\":8,\"name\":\"New film8\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[8]}]"));

    }

    @Test
    public void getPopularWithoutCount() throws Exception {
        filmDbService.putLike(1, 1);
        filmDbService.putLike(2, 2);
        filmDbService.putLike(3, 3);
        filmDbService.putLike(4, 4);
        filmDbService.putLike(5, 5);
        filmDbService.putLike(6, 6);
        filmDbService.putLike(7, 7);
        filmDbService.putLike(8, 8);
        filmDbService.putLike(9, 9);
        filmDbService.putLike(10, 10);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/films/popular"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"New film\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[1]},{\"id\":2,\"name\":\"New film2\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[2]},{\"id\":3,\"name\":\"New film3\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[3]},{\"id\":4,\"name\":\"New film4\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[4]},{\"id\":5,\"name\":\"New film5\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[5]},{\"id\":6,\"name\":\"New film6\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[6]},{\"id\":7,\"name\":\"New film7\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[7]},{\"id\":8,\"name\":\"New film8\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[8]},{\"id\":9,\"name\":\"New film9\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[9]},{\"id\":10,\"name\":\"New film10\",\"description\":\"Some description\",\"releaseDate\":\"2020-10-13\",\"duration\":\"PT2H\",\"likes\":[10]}]"));

    }

    @AfterEach
    void delete() {
        filmStorage.deleteAll();
        userStorage.deleteAll();
    }
}
