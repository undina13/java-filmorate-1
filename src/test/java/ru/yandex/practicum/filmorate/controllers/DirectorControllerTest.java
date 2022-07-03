package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorControllerTest {


    @Autowired
    private DirectorDbStorage directorDbStorage;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DirtiesContext
    public void shouldAddDirectorTest() throws Exception {
        Director director = new Director(
                1,
                "Director");

        mockMvc.perform(
                        post("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Director"));
    }

    @Test
    public void shouldNotAddDirectorDirector() throws Exception {
        Director director = new Director(
                1,
                "");

        mockMvc.perform(
                        post("/directors")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateDirector() throws Exception {
        Director director1 = new Director(
                1,
                "Director");
        Director director2 = new Director(
                1,
                "New Director");

        directorDbStorage.addDirector(director1);
        directorDbStorage.updateDirector(director2);

        mockMvc.perform(
                        put("/directors")
                                .content(objectMapper.writeValueAsString(director2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Director"));
    }

    @Test
    @DirtiesContext
    public void shouldNotUpdateDirector() throws Exception {
        Director director1 = new Director(
                1,
                "Director");
        Director director2 = new Director(
                1,
                "");

        directorDbStorage.addDirector(director1);
        directorDbStorage.updateDirector(director2);

        mockMvc.perform(
                        put("/directors")
                                .content(objectMapper.writeValueAsString(director2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext
    public void shouldGetDirector() throws Exception {
        Director director = new Director(
                1,
                "Director");


        directorDbStorage.addDirector(director);

        mockMvc.perform(
                        get("/directors/3")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Director"));
    }

    @Test
    @DirtiesContext
    public void shouldNotGetDirector() throws Exception {
        Director director = new Director(
                1,
                "Director");


        directorDbStorage.addDirector(director);

        mockMvc.perform(
                        get("/directors/-3")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DirtiesContext
    public void shouldRemoveDirector() throws Exception {
        Director director = new Director(
                1,
                "Director");

        directorDbStorage.addDirector(director);
        directorDbStorage.removeDirector(director.getId());
        mockMvc.perform(
                        get("/directors/5")
                                .content(objectMapper.writeValueAsString(director))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

}
