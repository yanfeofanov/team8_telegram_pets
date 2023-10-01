package pro.sky.telegrambot.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userServiceMock;

    static final User user1 = new User(1L, "Alex", 1L, LocalDateTime.now());
    static final User user2 = new User(2L, "David", 2L, LocalDateTime.now());

    static final JSONObject jsO1 = new JSONObject();
    static final JSONObject jsO2 = new JSONObject();

    static {
        try {
            jsO1.put("id", user1.getId());
            jsO1.put("userName", user1.getUserName());
            jsO1.put("chatId", user1.getChatId());
            jsO1.put("dateAdded", user1.getDateAdded());
            jsO2.put("id", user2.getId());
            jsO2.put("userName", user2.getUserName());
            jsO2.put("chatId", user2.getChatId());
            jsO2.put("dateAdded", user2.getDateAdded());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllUsersTest() throws Exception {
        Collection<User> users = List.of(user1, user2);
        when(userServiceMock.getAllUsers()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].userName").value(user1.getUserName()))
                .andExpect(jsonPath("$[0].chatId").value(user1.getChatId()))
                .andExpect(jsonPath("$[0].dateAdded").value(user1.getDateAdded().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].userName").value(user2.getUserName()))
                .andExpect(jsonPath("$[1].chatId").value(user2.getChatId()))
                .andExpect(jsonPath("$[1].dateAdded").value(user2.getDateAdded().format(DateTimeFormatter.ISO_DATE_TIME)));
        verify(userServiceMock, new Times(1)).getAllUsers();
    }
}
