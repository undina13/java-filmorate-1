package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPAA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)


public class RecommendationTest {
    @Autowired
    UserController userController;
    @Autowired
    FilmController filmController;
    @Autowired
    FilmDbStorage filmDbStorage;

    @Autowired
    private MockMvc mockMvc;

//пользователю 3 рекомендуются фильмы 4, 6, 2 тк у них пересечение с пользователем 2 по оценке фильма 3
    @Test
    void getRecommendationUser3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/3/recommendations"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":4,\"name\":\"Фильм4\",\"description\":\"какое-то описание\"," +
                                "\"releaseDate\":\"2022-03-15\",\"duration\":180,\"marks\":[{\"user_id\":2,\"film_id\":4,\"mark\":8}," +
                                "{\"user_id\":1,\"film_id\":4,\"mark\":7}],\"genres\":null," +
                                "\"directors\":[],\"mpa\":{\"id\":1,\"name\":\"G\"},\"rate\":7.5}," +
                                   "{\"id\":6,\"name\":\"Фильм6\",\"description\":\"какое-то описание\"," +
                                "\"releaseDate\":\"2020-08-16\",\"duration\":120,\"marks\":" +
                                "[{\"user_id\":2,\"film_id\":6,\"mark\":10}],\"genres\":null,\"directors\":[]," +
                                "\"mpa\":{\"id\":5,\"name\":\"NC-17\"},\"rate\":10.0}," +
                                   "{\"id\":2,\"name\":\"Фильм2\"," +
                                "\"description\":\"какое-то описание\",\"releaseDate\":\"2022-01-16\"," +
                                "\"duration\":120,\"marks\":[{\"user_id\":2,\"film_id\":2,\"mark\":7}," +
                                "{\"user_id\":1,\"film_id\":2,\"mark\":9}],\"genres\":[{\"id\":5,\"name\":\"Документальный\"}]," +
                                "\"directors\":[{\"id\":2,\"name\":\"фильм1\"}],\"mpa\":{\"id\":4,\"name\":\"R\"},\"rate\":8.0}]"));
    }

    @Test
    // 1  5
    void getRecommendationUser2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/2/recommendations"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"Фильм1\",\"description\":\"какое-то описание\"," +
                                "\"releaseDate\":\"2022-03-15\",\"duration\":180,\"marks\":" +
                                "[{\"user_id\":3,\"film_id\":1,\"mark\":10}],\"genres\":[{\"id\":1,\"name\":\"Комедия\"}," +
                                "{\"id\":3,\"name\":\"Мультфильм\"}],\"directors\":[{\"id\":1,\"name\":\"Режиссер1\"}]," +
                                "\"mpa\":{\"id\":1,\"name\":\"G\"},\"rate\":10.0}," +
                                "    {\"id\":5,\"name\":\"Фильм5\",\"description\":\"какое-то описание\"," +
                                "\"releaseDate\":\"2022-01-16\",\"duration\":120," +
                                "\"marks\":[{\"user_id\":3,\"film_id\":5,\"mark\":8}],\"genres\":null,\"directors\":[]," +
                                "\"mpa\":{\"id\":4,\"name\":\"R\"},\"rate\":8.0}]"));
    }

    @Test
          void getRecommendationUser1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/recommendations"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":6,\"name\":\"Фильм6\",\"description\":\"какое-то описание\"," +
                                "\"releaseDate\":\"2020-08-16\",\"duration\":120,\"marks\":" +
                                "[{\"user_id\":2,\"film_id\":6,\"mark\":10}],\"genres\":null,\"directors\":[]," +
                                "\"mpa\":{\"id\":5,\"name\":\"NC-17\"},\"rate\":10.0} ]"));
    }


}
