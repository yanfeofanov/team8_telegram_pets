package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.exception.CommunicationRequestNotFoundException;
import pro.sky.telegrambot.model.CommunicationRequest;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.CommunicationRequestRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunicationRequestServiceTest {

    private final UserService userServiceMock = mock(UserService.class);
    private final VolunteerService volunteerServiceMock = mock(VolunteerService.class);
    private final CommunicationRequestRepository communicationRequestRepositoryMock = mock(CommunicationRequestRepository.class);
    private final CommunicationRequestService out = new CommunicationRequestService(communicationRequestRepositoryMock, userServiceMock, volunteerServiceMock);
    private final User testUser = new User(1L, "login", 1L, LocalDateTime.now());
    private final CommunicationRequest testCommunicationRequest1 = new CommunicationRequest(LocalDateTime.now(), testUser, "номер телефона", false, new Volunteer());
    private final CommunicationRequest testCommunicationRequest2 = new CommunicationRequest(LocalDateTime.now(), testUser, "адрес электронной почты", false, new Volunteer());

    @Test
    void getAllRequestForPeriodByDoneTest() {
        Collection<CommunicationRequest> requests = List.of(testCommunicationRequest1, testCommunicationRequest2);
        when(communicationRequestRepositoryMock.findByDateBetweenAndDone(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean())).thenReturn(requests);
        assertThat(out.getAllRequestForPeriodByDone(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean()))
                .isEqualTo(requests);
        verify(communicationRequestRepositoryMock, new Times(1)).findByDateBetweenAndDone(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean());
    }

    @Test
    void addRequestToDatabaseTestWhenRequestAlreadyExists() {
        when(userServiceMock.findUserById(anyLong())).thenReturn(testUser);
        when(communicationRequestRepositoryMock.findByUserAndDoneIsFalse(any(User.class))).thenReturn(testCommunicationRequest1);
        when(communicationRequestRepositoryMock.save(any(CommunicationRequest.class))).thenReturn(testCommunicationRequest1);
        assertThat(out.addRequestToDatabase(anyLong(), "номер телефона"))
                .isNotNull()
                .isEqualTo(testCommunicationRequest1);
        verify(userServiceMock, new Times(1)).findUserById(anyLong());
        verify(communicationRequestRepositoryMock, new Times(1)).findByUserAndDoneIsFalse(any(User.class));
        verify(communicationRequestRepositoryMock, new Times(1)).save(any(CommunicationRequest.class));
    }

    @Test
    void addRequestToDatabaseTestWhenThereIsNoRequest() {
        when(userServiceMock.findUserById(anyLong())).thenReturn(testUser);
        when(communicationRequestRepositoryMock.findByUserAndDoneIsFalse(any(User.class))).thenReturn(null);
        when(communicationRequestRepositoryMock.save(any(CommunicationRequest.class))).thenReturn(testCommunicationRequest1);
        assertThat(out.addRequestToDatabase(anyLong(), "номер телефона"))
                .isNotNull()
                .isEqualTo(testCommunicationRequest1);
        verify(userServiceMock, new Times(1)).findUserById(anyLong());
        verify(communicationRequestRepositoryMock, new Times(1)).findByUserAndDoneIsFalse(any(User.class));
        verify(communicationRequestRepositoryMock, new Times(1)).save(any(CommunicationRequest.class));
    }

    @Test
    void addRequestToDatabaseNegativeTest() {
        when(userServiceMock.findUserById(anyLong())).thenReturn(null);
        assertThat(out.addRequestToDatabase(anyLong(), "номер телефона"))
                .isNull();
        verify(userServiceMock, new Times(1)).findUserById(anyLong());
        verify(communicationRequestRepositoryMock, new Times(0)).findByUserAndDoneIsFalse(any(User.class));
        verify(communicationRequestRepositoryMock, new Times(0)).save(any(CommunicationRequest.class));
    }

    @Test
    void checkCommunicationRequestPositiveTest() {
        when(communicationRequestRepositoryMock.findById(anyInt())).thenReturn(Optional.of(testCommunicationRequest1));
        when(communicationRequestRepositoryMock.save(any(CommunicationRequest.class))).thenReturn(testCommunicationRequest1);
        assertEquals(out.checkCommunicationRequest(anyInt(), false), testCommunicationRequest1);
        verify(communicationRequestRepositoryMock, new Times(1)).findById(anyInt());
        verify(communicationRequestRepositoryMock, new Times(1)).save(any(CommunicationRequest.class));
    }

    @Test
    void checkCommunicationRequestNegativeTest() {
        when(communicationRequestRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CommunicationRequestNotFoundException.class, () -> out.checkCommunicationRequest(anyInt(), true));
        verify(communicationRequestRepositoryMock, new Times(1)).findById(anyInt());
    }
}