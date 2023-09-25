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
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.service.PetService;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PetService petServiceMock;

    static final Shelter shelter1 = new Shelter("Приют1", TypeOfPet.CAT);
    static final Shelter shelter2 = new Shelter("Приют2", TypeOfPet.DOG);
    static final Pet pet1 = new Pet(TypeOfPet.CAT.toString(), "Пушок", (byte) 3, "Британец", shelter1, false);
    static final Pet pet2 = new Pet(TypeOfPet.DOG.toString(), "Чушок", (byte) 1, "Дворовой", shelter2, false);
    static final JSONObject pet1Json = new JSONObject();
    static final JSONObject pet2Json = new JSONObject();

    static {
        try {
            pet1Json.put("id", "1");
            pet1Json.put("type", pet1.getName());
            pet1Json.put("name", pet1.getType());
            pet1Json.put("age", pet1.getAge());
            pet1Json.put("breed", pet1.getBreed());
            pet1Json.put("shelter", pet1.getShelter());
            pet1Json.put("leave", pet1.getLeave());

            pet2Json.put("id", "2");
            pet2Json.put("type", pet2.getName());
            pet2Json.put("name", pet2.getType());
            pet2Json.put("age", pet2.getAge());
            pet2Json.put("breed", pet2.getBreed());
            pet2Json.put("shelter", pet2.getShelter());
            pet2Json.put("leave", pet2.getLeave());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findPetPositiveTest() throws Exception {
        when(petServiceMock.findPetById(anyInt())).thenReturn(pet1);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pet1.getId()))
                .andExpect(jsonPath("$.name").value(pet1.getName()))
                .andExpect(jsonPath("$.type").value(pet1.getType()))
                .andExpect(jsonPath("$.age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$.breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$.leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$.shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$.shelter.name").value(pet1.getShelter().getName()));
        verify(petServiceMock, new Times(1)).findPetById(anyInt());
    }

    @Test
    void findPetNegativeTest() throws Exception {
        /*mockMvc.perform(MockMvcRequestBuilders.get("/pets/"))
                .andExpect(status().isBadRequest());*/
    }

    @Test
    void addPetTest() throws Exception {
       /* when(petServiceMock.addPet(any(Pet.class))).thenReturn(pet1);
        mockMvc.perform(MockMvcRequestBuilders.post("/pets/add")
                        .content(pet1Json.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pet1.getId()))
                .andExpect(jsonPath("$.name").value(pet1.getName()))
                .andExpect(jsonPath("$.type").value(pet1.getType()))
                .andExpect(jsonPath("$.age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$.breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$.leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$.shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$.shelter.name").value(pet1.getShelter().getName()));
        verify(petServiceMock, new Times(1)).addPet(any(Pet.class));*/
    }

    @Test
    void deletePet() {
    }

    @Test
    void getAllPetsTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.getAllPets()).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).getAllPets();
    }

    @Test
    void getAllPetsThatHaveLeftShelterTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.getAllPetsThatHaveLeftShelter()).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/leave"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).getAllPetsThatHaveLeftShelter();
    }

    @Test
    void gatAllAvailableTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.getAllAvailable()).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).getAllAvailable();
    }

    @Test
    void gatAllOnProbationTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.gatAllOnProbation()).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/on_probation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).gatAllOnProbation();
    }

    @Test
    void getAllPetsByTypePositiveTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.getAllPetsByType(any(TypeOfPet.class))).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/cat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).getAllPetsByType(any(TypeOfPet.class));
    }

    @Test
    void getAllPetsByTypeNegativeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/c"))
                .andExpect(status().isBadRequest());
        verify(petServiceMock, new Times(0)).getAllPetsByType(any(TypeOfPet.class));
    }

    @Test
    void getAllAvailablePetsByTypePositiveTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.getAllAvailablePetsByType(any(TypeOfPet.class))).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/dog/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).getAllAvailablePetsByType(any(TypeOfPet.class));
    }

    @Test
    void getAllAvailablePetsByTypeNegativeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/doggg/available"))
                .andExpect(status().isBadRequest());
        verify(petServiceMock, new Times(0)).getAllAvailablePetsByType(any(TypeOfPet.class));
    }

    @Test
    void getPetsByOwnerIdTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.findPetsByPetOwnerId(anyInt())).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).findPetsByPetOwnerId(anyInt());
    }

    @Test
    void getPetOnProbationByOwnerIdTest() throws Exception {
        when(petServiceMock.findPetOnProbationByPetOwnerId(anyInt())).thenReturn(pet1);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/on_probation/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pet1.getId()))
                .andExpect(jsonPath("$.name").value(pet1.getName()))
                .andExpect(jsonPath("$.type").value(pet1.getType()))
                .andExpect(jsonPath("$.age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$.breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$.leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$.shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$.shelter.name").value(pet1.getShelter().getName()));
        verify(petServiceMock, new Times(1)).findPetOnProbationByPetOwnerId(anyInt());
    }

    @Test
    void getAllPetsOnProbationByTypePositiveTest() throws Exception {
        Collection<Pet> pets = List.of(pet1, pet2);
        when(petServiceMock.getAllPetsOnProbationByType(any(TypeOfPet.class))).thenReturn(pets);
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/cAt/on_probation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pet1.getId()))
                .andExpect(jsonPath("$[0].name").value(pet1.getName()))
                .andExpect(jsonPath("$[0].type").value(pet1.getType()))
                .andExpect(jsonPath("$[0].age").value((int) pet1.getAge()))
                .andExpect(jsonPath("$[0].breed").value(pet1.getBreed()))
                .andExpect(jsonPath("$[0].leave").value(pet1.getLeave()))
                .andExpect(jsonPath("$[0].shelter.id").value(pet1.getShelter().getId()))
                .andExpect(jsonPath("$[0].shelter.name").value(pet1.getShelter().getName()))
                .andExpect(jsonPath("$[1].id").value(pet2.getId()))
                .andExpect(jsonPath("$[1].name").value(pet2.getName()))
                .andExpect(jsonPath("$[1].type").value(pet2.getType()))
                .andExpect(jsonPath("$[1].age").value((int) pet2.getAge()))
                .andExpect(jsonPath("$[1].breed").value(pet2.getBreed()))
                .andExpect(jsonPath("$[1].leave").value(pet2.getLeave()))
                .andExpect(jsonPath("$[1].shelter.id").value(pet2.getShelter().getId()))
                .andExpect(jsonPath("$[1].shelter.name").value(pet2.getShelter().getName()));
        verify(petServiceMock, new Times(1)).getAllPetsOnProbationByType(any(TypeOfPet.class));
    }

    @Test
    void getAllPetsOnProbationByTypeNegativeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all/c t/on_probation"))
                .andExpect(status().isBadRequest());
        verify(petServiceMock, new Times(0)).getAllPetsOnProbationByType(any(TypeOfPet.class));
    }
}