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
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.service.InfoService;
import pro.sky.telegrambot.service.PetService;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InfoController.class)
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InfoService infoServiceMock;

    // static final int id = 0;
    static final Shelter shelter1 = new Shelter("Приют1", TypeOfPet.CAT);
    static final Shelter shelter2 = new Shelter("Приют2", TypeOfPet.DOG);

    static final Info info1 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, "текст1", shelter1);
    static final Info info2 = new Info(TypesOfInformation.LONG_INFO_ABOUT_SHELTER, "текст2", shelter2);

    static final JSONObject shelter1JSON = new JSONObject();
    static final JSONObject shelter2JSON = new JSONObject();
    static final JSONObject info1JSON = new JSONObject();
    static final JSONObject info2JSON = new JSONObject();

    static {
        try {
            shelter1JSON.put("id", shelter1.getId());
            shelter1JSON.put("type", shelter1.getType());
            shelter1JSON.put("name", shelter1.getName());
            shelter2JSON.put("id", shelter2.getId());
            shelter2JSON.put("type", shelter2.getType());
            shelter2JSON.put("name", shelter2.getName());
            info1JSON.put("id", info1.getId());
            info1JSON.put("shelter", shelter1JSON);
            info2JSON.put("shelter", shelter2JSON);
            info1JSON.put("text", info1.getText());
            info1JSON.put("type", info1.getType());
            info2JSON.put("id", info2.getId());
            info2JSON.put("text", info2.getText());
            info2JSON.put("type", info2.getType());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addInfoTest() throws Exception {
        when(infoServiceMock.addInfo(any(Info.class))).thenReturn(info1);
        mockMvc.perform(MockMvcRequestBuilders.post("/info/addInfo")
                        .content(info1JSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(info1.getId()))
                .andExpect(jsonPath("$.text").value(info1.getText()));
        verify(infoServiceMock, new Times(1)).addInfo(any(Info.class));
    }

    @Test
    void updateInfoTest() throws Exception {
        when(infoServiceMock.updateInfoShelter(anyInt(), any(Info.class))).thenReturn(info1);
        mockMvc.perform(MockMvcRequestBuilders.put("/info/update/" + info1.getId())
                        .content(info1JSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(info1.getId()))
                .andExpect(jsonPath("$.text").value(info1.getText()))
                .andExpect(jsonPath("$.type").value(info1.getType().toString()))
                .andExpect(jsonPath("$.shelter.id").value(shelter1.getId()));
        verify(infoServiceMock, new Times(1)).updateInfoShelter(info1.getId(), info1);
    }

    @Test
    void getInfoAllShelterTest() throws Exception {
        Collection<Info> infoAll = List.of(info1,info2);
        when(infoServiceMock.findByShelterIdInfo(anyInt())).thenReturn(infoAll);
        mockMvc.perform(MockMvcRequestBuilders.get("/info/" + info1.getId())
                        .content(info1JSON.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(info1.getId()))
                .andExpect(jsonPath("$[0].text").value(info1.getText()))
                .andExpect(jsonPath("$[0].type").value(info1.getType().toString()))
                .andExpect(jsonPath("$[1].id").value(info2.getId()))
                .andExpect(jsonPath("$[1].text").value(info2.getText()))
                .andExpect(jsonPath("$[1].type").value(info2.getType().toString()));
        verify(infoServiceMock, new Times(1)).findByShelterIdInfo(anyInt());
    }
}
