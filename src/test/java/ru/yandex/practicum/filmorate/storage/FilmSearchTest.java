package ru.yandex.practicum.filmorate.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controllers.DirectorController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorFilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.TreeSet;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class FilmSearchTest {

    @Autowired
    private DirectorDbStorage directorDbStorage;
    @Autowired
    private DirectorFilmDbStorage directorFilmDbStorage;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void SearchByDirector() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/films/search?query=реЖиссЕр1&by=director"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"Фильм1\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-03-15\",\"duration\":180,\"likes\":[3],\"genres\":[{\"id\":1,\"name\":\"Комедия\"},{\"id\":3,\"name\":\"Мультфильм\"}],\"directors\":[{\"id\":1,\"name\":\"Режиссер1\"}],\"mpa\":{\"id\":1,\"name\":\"G\"}}]"));
    }

    @Test
    void SearchByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search?query=Фильм1&by=title"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"Фильм1\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-03-15\",\"duration\":180,\"likes\":[3],\"genres\":[{\"id\":1,\"name\":\"Комедия\"},{\"id\":3,\"name\":\"Мультфильм\"}],\"directors\":[{\"id\":1,\"name\":\"Режиссер1\"}],\"mpa\":{\"id\":1,\"name\":\"G\"}}]"));
    }

    @Test
    void SearchByTitleAndDirector() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films/search?query=Фильм1&by=director,title"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":2,\"name\":\"Фильм2\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-01-16\",\"duration\":120,\"likes\":[1,2],\"genres\":[{\"id\":5,\"name\":\"Документальный\"}],\"directors\":[{\"id\":2,\"name\":\"фильм1\"}],\"mpa\":{\"id\":4,\"name\":\"R\"}},{\"id\":1,\"name\":\"Фильм1\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-03-15\",\"duration\":180,\"likes\":[3],\"genres\":[{\"id\":1,\"name\":\"Комедия\"},{\"id\":3,\"name\":\"Мультфильм\"}],\"directors\":[{\"id\":1,\"name\":\"Режиссер1\"}],\"mpa\":{\"id\":1,\"name\":\"G\"}}]"));
    }


}
