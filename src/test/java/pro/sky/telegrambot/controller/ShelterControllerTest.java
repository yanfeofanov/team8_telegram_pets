package pro.sky.telegrambot.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.service.PetService;
import pro.sky.telegrambot.service.ShelterService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShelterController.class)
class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShelterService shelterService;

    static private final Shelter testShelter = new Shelter("тестовый приют", TypeOfPet.CAT);
    static private final JSONObject testShelterJson = new JSONObject();

    static {
        try {
            testShelterJson.put("id", testShelter.getId());
            testShelterJson.put("name", testShelter.getName());
            testShelterJson.put("type", testShelter.getType());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addShelterTest() throws Exception {
        when(shelterService.addShelter(any(Shelter.class))).thenReturn(testShelter);
        mockMvc.perform(MockMvcRequestBuilders.post("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testShelterJson.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testShelter.getId()))
                .andExpect(jsonPath("$.name").value(testShelter.getName()))
                .andExpect(jsonPath("$.type").value(testShelter.getType().toString()));
        verify(shelterService, new Times(1)).addShelter(any(Shelter.class));
    }

    @Test
    void deleteShelterTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("" + testShelter.getId()))
                .andExpect(status().isOk());
        verify(shelterService, new Times(1)).deleteShelterById(anyInt());
    }

    @Test
    void replaceShelterTest() throws Exception {
        when(shelterService.updateShelter(any(Shelter.class))).thenReturn(testShelter);
        mockMvc.perform(MockMvcRequestBuilders.put("/shelter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testShelterJson.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testShelter.getId()))
                .andExpect(jsonPath("$.name").value(testShelter.getName()))
                .andExpect(jsonPath("$.type").value(testShelter.getType().toString()));
        verify(shelterService, new Times(1)).updateShelter(any(Shelter.class));
    }
}