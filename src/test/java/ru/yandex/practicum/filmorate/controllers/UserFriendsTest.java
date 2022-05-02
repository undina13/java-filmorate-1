package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserFriendsTest {
    @Autowired
    UserController userController;
    @Autowired
    UserStorage userStorage;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void deleteAll() {
        userStorage.deleteAll();
    }

    @Test
    void getCommonFriends() throws Exception {
        userStorage.put(FilmUserTestData.user1);
        userStorage.put(FilmUserTestData.user2);
        userStorage.put(FilmUserTestData.user3);
        userStorage.put(FilmUserTestData.user4);
        userController.putFriends(1, 2);
        userController.putFriends(1, 3);
        userController.putFriends(4, 2);
        userController.putFriends(4, 3);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/2/friends/common/3"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"email\":\"dfg@mail.ru\",\"login\":\"login\",\"name\":\"name\",\"birthday\":\"1980-05-13\", \"friends\":[2,3]}," +
                                "{\"id\":4,\"email\":\"dfg4@mail.ru\",\"login\":\"login4\",\"name\":\"name4\",\"birthday\":\"1980-05-13\",\"friends\":[2,3]} ]"));
    }

    @Test
    void getCommonFriendsBadFriend() throws Exception {
        userStorage.put(FilmUserTestData.user1);
        userStorage.put(FilmUserTestData.user2);
        userStorage.put(FilmUserTestData.user3);
        userStorage.put(FilmUserTestData.user4);
        userController.putFriends(1, 2);
        userController.putFriends(1, 3);
        userController.putFriends(4, 2);
        userController.putFriends(4, 3);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/2/friends/common/13"))
                .andExpect(status().isNotFound());

    }
    @Test
    void getFriends() throws Exception {
        userStorage.put(FilmUserTestData.user1);
        userStorage.put(FilmUserTestData.user2);
        userStorage.put(FilmUserTestData.user3);
        userStorage.put(FilmUserTestData.user4);
        userController.putFriends(1, 2);
        userController.putFriends(1, 3);
        userController.putFriends(4, 2);
        userController.putFriends(4, 3);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/1/friends"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":2,\"email\":\"dfg2@mail.ru\",\"login\":\"login2\",\"name\":\"name2\",\"birthday\":\"1980-05-13\",\"friends\":[1,4]},{\"id\":3,\"email\":\"dfg3@mail.ru\",\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"1980-05-13\",\"friends\":[1,4]}]"));
    }


    @Test
    void userDeleteTest() throws Exception {
        userStorage.put(FilmUserTestData.user5);
        userStorage.put(FilmUserTestData.user6);
        userStorage.put(FilmUserTestData.user7);
        userController.putFriends(5, 6);
        userController.putFriends(5, 7);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/5/friends"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":6,\"email\":\"dfg6@mail.ru\",\"login\":\"login6\",\"name\":\"name6\",\"birthday\":\"1980-05-13\",\"friends\":[5]},{\"id\":7,\"email\":\"dfg7@mail.ru\",\"login\":\"login7\",\"name\":\"name7\",\"birthday\":\"1980-05-13\",\"friends\":[5]}]"));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/6/friends/5"))
                .andExpect(status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/5/friends"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":7,\"email\":\"dfg7@mail.ru\",\"login\":\"login7\",\"name\":\"name7\",\"birthday\":\"1980-05-13\",\"friends\":[5]}]"));
    }


    @AfterEach
    void delete() {
        userStorage.deleteAll();
    }
}
