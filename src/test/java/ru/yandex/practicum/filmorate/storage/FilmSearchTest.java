package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmSearchTest {
    @Autowired
    FilmController filmController;
    @Autowired
    FilmDbStorage filmStorage;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup(){

    }

//    @Test
//    void searchByDirector() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/films/search?query=нОвЫй&by=director"))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(content()
//                        .json("[{\"id\":1,\"name\":\"Фильм1\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-03-15\",\"duration\":180,\"likes\":[3],\"genres\":[{\"id\":1,\"name\":\"Комедия\"},{\"id\":3,\"name\":\"Мультфильм\"}],\"mpa\":{\"id\":1,\"name\":\"G\"}},{\"id\":2,\"name\":\"Фильм2\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-01-16\",\"duration\":120,\"likes\":[1,2],\"genres\":[{\"id\":5,\"name\":\"Документальный\"}],\"mpa\":{\"id\":4,\"name\":\"R\"}},{\"id\":3,\"name\":\"Фильм3\",\"description\":\"какое-то описание\",\"releaseDate\":\"2020-08-16\",\"duration\":120,\"likes\":[2],\"genres\":[{\"id\":2,\"name\":\"Драма\"},{\"id\":4,\"name\":\"Триллер\"}],\"mpa\":{\"id\":5,\"name\":\"NC-17\"}}]"));
//
//    }
}
