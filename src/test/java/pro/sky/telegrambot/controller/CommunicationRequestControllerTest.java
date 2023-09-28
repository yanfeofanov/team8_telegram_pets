package pro.sky.telegrambot.controller;

import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.model.CommunicationRequest;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.service.CommunicationRequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunicationRequestController.class)
class CommunicationRequestControllerTest {

    private final static LocalDateTime startPeriod = LocalDateTime.now().minusDays(1);
    private final static LocalDateTime endPeriod = LocalDateTime.now();
    private final static CommunicationRequest request1 = new CommunicationRequest(LocalDateTime.now(), new User(), "89053249321", false, new Volunteer());
    private final static CommunicationRequest request2 = new CommunicationRequest(LocalDateTime.now(), new User(), "mail@gmail.com", false, new Volunteer());

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CommunicationRequestService communicationRequestServiceMock;

    @Test
    void getAllForPeriodByDonePositiveTest() throws Exception {
        Collection<CommunicationRequest> requests = List.of(request1, request2);
        when(communicationRequestServiceMock.getAllRequestForPeriodByDone(any(LocalDateTime.class), any(LocalDateTime.class), anyBoolean()))
                .thenReturn(requests)
                .thenReturn(requests);
        mockMvc.perform(MockMvcRequestBuilders.get("/communication_request/all_for_period?startPeriod=" + startPeriod + "&done=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(request1.getId()))
                .andExpect(jsonPath("$[0].date").value(request1.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].user.id").value(request1.getUser().getId()))
                .andExpect(jsonPath("$[0].contactInfo").value(request1.getContactInfo()))
                .andExpect(jsonPath("$[0].done").value(request1.getDone()))
                .andExpect(jsonPath("$[0].volunteer.id").value(request1.getVolunteer().getId()))
                .andExpect(jsonPath("$[1].id").value(request2.getId()))
                .andExpect(jsonPath("$[1].date").value(request2.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[1].user.id").value(request2.getUser().getId()))
                .andExpect(jsonPath("$[1].contactInfo").value(request2.getContactInfo()))
                .andExpect(jsonPath("$[1].done").value(request2.getDone()))
                .andExpect(jsonPath("$[1].volunteer.id").value(request2.getVolunteer().getId()));
        mockMvc.perform(MockMvcRequestBuilders.get("/communication_request/all_for_period?startPeriod=" + startPeriod + "&endPeriod=" + endPeriod + "&done=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(request1.getId()))
                .andExpect(jsonPath("$[0].date").value(request1.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].user.id").value(request1.getUser().getId()))
                .andExpect(jsonPath("$[0].contactInfo").value(request1.getContactInfo()))
                .andExpect(jsonPath("$[0].done").value(request1.getDone()))
                .andExpect(jsonPath("$[0].volunteer.id").value(request1.getVolunteer().getId()))
                .andExpect(jsonPath("$[1].id").value(request2.getId()))
                .andExpect(jsonPath("$[1].date").value(request2.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[1].user.id").value(request2.getUser().getId()))
                .andExpect(jsonPath("$[1].contactInfo").value(request2.getContactInfo()))
                .andExpect(jsonPath("$[1].done").value(request2.getDone()))
                .andExpect(jsonPath("$[1].volunteer.id").value(request2.getVolunteer().getId()));
        verify(communicationRequestServiceMock, new Times(2)).getAllRequestForPeriodByDone(any(LocalDateTime.class), any(), anyBoolean());
    }

    @Test
    void getAllForPeriodByDoneNegativeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/communication_request/all_for_period?startPeriod=aaa&done=false"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadParamException));
    }

    @Test
    void checkCommunicationRequest() throws Exception {
        when(communicationRequestServiceMock.checkCommunicationRequest(anyInt(), anyBoolean())).thenReturn(request1);
        mockMvc.perform(MockMvcRequestBuilders.post("/communication_request/" + request1.getId() + "/check")
                        .content("true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request1.getId()))
                .andExpect(jsonPath("$.date").value(request1.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.user.id").value(request1.getUser().getId()))
                .andExpect(jsonPath("$.contactInfo").value(request1.getContactInfo()))
                .andExpect(jsonPath("$.done").value(request1.getDone()))
                .andExpect(jsonPath("$.volunteer.id").value(request1.getVolunteer().getId()));
        verify(communicationRequestServiceMock, new Times(1)).checkCommunicationRequest(anyInt(), anyBoolean());
    }
}