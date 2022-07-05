package ru.yandex.practicum.filmorate.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Mark;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDBService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmsLikesTest {
    @Autowired
    FilmDbService filmDbService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    public void putMarkAllOk() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/films/1/mark/1/6"))
                .andExpect(status().isOk());

        Assertions.assertEquals(filmDbService.get(1).getMarks(), Set.of(new Mark(1,1,6)));
    }

    @Test
    public void putTooBigMark() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/1/mark/1/60"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void putTooSmallMark() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/1/mark/1/-5"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void deleteMarkAllOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/1/mark/1/6"))
                .andExpect(status().isOk());
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/films/1/mark/1"))
                .andExpect(status().isOk());
        Assertions.assertNull(filmDbService.get(1).getMarks());
    }

    @Test
    public void deleteMarkNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/1/mark/15"))
                .andExpect(status().isInternalServerError());

    }
}
