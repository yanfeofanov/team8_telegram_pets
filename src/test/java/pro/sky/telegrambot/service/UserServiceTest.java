package pro.sky.telegrambot.service;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final UserRepository userRepositoryMock = mock(UserRepository.class);
    private final UserService out = new UserService(userRepositoryMock);

    private final User user1 = new User(1L, "User1", 1L, LocalDateTime.now());
    private final User user2 = new User(2L, "User2", 2L, LocalDateTime.now());

    @Test
    void addUserTest() {
        when(userRepositoryMock.save(any(User.class))).thenReturn(user1);
        Assertions.assertThat(user1).isEqualTo(out.addNewUser(1L, "a", "a", 1L));
    }

    @Test
    void isTheUserNewTest() {
        when(userRepositoryMock.findUserById(anyLong())).thenReturn(user1);
        assertThat(out.isTheUserNew(anyLong()))
                .isFalse();
        verify(userRepositoryMock, new Times(1)).findUserById(anyLong());
    }

    @Test
    void findByUserIdTest() {
        when(userRepositoryMock.findUserById(anyLong())).thenReturn(user1);
        assertThat(out.findUserById(anyLong()))
                .isNotNull()
                .isEqualTo(user1);
        verify(userRepositoryMock, new Times(1)).findUserById(anyLong());
    }

    @Test
    void getAllUserService() {
        List<User> user3 = List.of(user1, user2);
        when(userRepositoryMock.findAll()).thenReturn(user3);
        assertThat(out.getAllUsers())
                .isEqualTo(user3);
        verify(userRepositoryMock, new Times(1)).findAll();
    }
}