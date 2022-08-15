package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDBService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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

    @Test
    @DirtiesContext
    public void putLikeAllOk() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/films/1/like/1"))
                .andExpect(status().isOk());
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(1, 2, 3));
    }

    @Test
    @DirtiesContext
    public void deleteLikeAllOk() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/films/2/like/1"))
                .andExpect(status().isOk());
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(2, 3));
    }
}
