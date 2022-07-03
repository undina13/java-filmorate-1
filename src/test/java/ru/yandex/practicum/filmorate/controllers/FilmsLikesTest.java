package ru.yandex.practicum.filmorate.controllers;

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
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(1, 3));
    }

    @Test
    @DirtiesContext
    public void deleteLikeAllOk() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/films/2/like/1"))
                .andExpect(status().isOk());
        Assertions.assertEquals(filmDbService.get(1).getLikes(), Set.of(3));
    }

//    @Test
//    @DirtiesContext
//    public void getPopular() throws Exception {
//        filmDbService.putLike(1, 1);
//        filmDbService.putLike(1, 2);
//        filmDbService.putLike(2, 3);
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/films/popular?count=2"))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(content()
//                   //     .json("[{\"id\":2,\"name\":\"Фильм2\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-01-16\",\"duration\":120,\"likes\":[1,2,3],\"genres\":[{\"id\":5,\"name\":\"Документальный\"}],\"directors\":[],\"mpa\":{\"id\":4,\"name\":\"2\"}},{\"id\":3,\"name\":\"Фильм3\",\"description\":\"какое-то описание\",\"releaseDate\":\"2020-08-16\",\"duration\":120,\"likes\":[2],\"genres\":[{\"id\":2,\"name\":\"Драма\"},{\"id\":4,\"name\":\"Триллер\"}],\"directors\":[],\"mpa\":{\"id\":5,\"name\":\"3\"}}]"));
//
//                       .json(" [{\"id\":2,\"name\":\"Фильм2\",\"description\":\"какое-то описание\",\"releaseDate\":\"2022-01-16\",\"duration\":120,\"likes\":[1,2,3],\"genres\":[{\"id\":5,\"name\":\"Документальный\"}],\"directors\":[],\"mpa\":{\"id\":4,\"name\":\"2\"}},{\"id\":2,\"name\":\"�����2\",\"description\":\"�����-�� ��������\",\"releaseDate\":\"2022-01-16\",\"duration\":120,\"likes\":[1,2,3],\"genres\":[{\"id\":5,\"name\":\"��������������\"}],\"directors\":[],\"mpa\":{\"id\":4,\"name\":\"2\"}}]"));
//    }
}
