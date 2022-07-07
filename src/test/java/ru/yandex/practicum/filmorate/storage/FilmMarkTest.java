package ru.yandex.practicum.filmorate.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Mark;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmMarkTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    FilmController filmController;


    @Test
    @DirtiesContext
    public void updateMarkToLower() throws Exception {
        //при добавлении маленькой оценки рейтинг фильма падает
        mockMvc.perform(
                MockMvcRequestBuilders.put("/films/1/mark/1/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/films/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"Фильм1\",\"description\":\"какое-то описание\"," +
                                "\"releaseDate\":\"2022-03-15\",\"duration\":180,\"marks\":" +
                                "[{\"user_id\":3,\"film_id\":1,\"mark\":10},{\"user_id\":1,\"film_id\":1,\"mark\":1}]," +
                                "\"genres\":" +
                                "[{\"id\":1,\"name\":\"Комедия\"},{\"id\":3,\"name\":\"Мультфильм\"}],\"mpa\":" +
                                "{\"id\":1,\"name\":\"G\"},\"rate\":5.5}  "));
    }

    @Test
    @DirtiesContext
    public void updateRateAfterDeleteMark() throws Exception {
        //при удалении оценки рейтинг пересчитывается
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/2/mark/2"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/films/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":2,\"name\":\"Фильм2\",\"description\":" +
                                "\"какое-то описание\",\"releaseDate\":\"2022-01-16\",\"duration\":120," +
                                "\"marks\":[{\"user_id\":1,\"film_id\":2,\"mark\":9}]," +
                                "\"genres\":[{\"id\":5,\"name\":\"Документальный\"}]," +
                                "\"directors\":[{\"id\":2,\"name\":\"фильм1\"}],\"mpa\":{\"id\":4,\"name\":\"R\"},\"rate\":9.0}"));

    }

}
