package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controllers.UserController;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventStorageTest {
    @Autowired
    UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DirtiesContext
    void findFeedByUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/feed"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"eventId\":1,\"timestamp\":123344556,\"userId\":1,\"eventType\":\"LIKE\"," +
                                "\"operation\":\"ADD\",\"entityId\":1},{\"eventId\":2,\"timestamp\":123344558," +
                                "\"userId\":1,\"eventType\":\"REVIEW\",\"operation\":\"ADD\",\"entityId\":1}]"));
    }
}
