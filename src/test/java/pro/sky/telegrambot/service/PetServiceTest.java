package pro.sky.telegrambot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.constant.TypeOfPet;
import pro.sky.telegrambot.model.Pet;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.PetRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    private final PetRepository petRepositoryMock = mock(PetRepository.class);
    private final PetService out = new PetService(petRepositoryMock);
    private final Pet testPet1 = new Pet(TypeOfPet.CAT.toString(), "Питомец1", (byte) 2, "порода1", new Shelter(), false);
    private final Pet testPet2 = new Pet(TypeOfPet.CAT.toString(), "Питомец2", (byte) 1, "порода2", new Shelter(), false);

    @Test
    void findPetByIdTest() {
        when(petRepositoryMock.findById(anyInt())).thenReturn(Optional.of(testPet1));
        assertThat(out.findPetById(anyInt()))
                .isNotNull()
                .isEqualTo(testPet1);
        verify(petRepositoryMock, new Times(1)).findById(anyInt());
    }

    @Test
    void addPetTest() {
        when(petRepositoryMock.save(any(Pet.class))).thenReturn(testPet1);
        assertThat(out.addPet(testPet1))
                .isNotNull()
                .isEqualTo(testPet1);
        verify(petRepositoryMock, new Times(1)).save(any(Pet.class));
    }

    @Test
    void deletePetTest() {
        out.deletePet(testPet1.getId());
        verify(petRepositoryMock, new Times(1)).deleteById(anyInt());
    }

    @Test
    void getAllPetsTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAll()).thenReturn(pets);
        assertThat(out.getAllPets())
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAll();
    }

    @Test
    void getAllPetsByTypeTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByType(anyString())).thenReturn(pets);
        assertThat(out.getAllPetsByType(TypeOfPet.DOG))
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByType(anyString());
    }

    @Test
    void findPetsByPetOwnerIdTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByPetOwnerId(anyInt())).thenReturn(pets);
        assertThat(out.findPetsByPetOwnerId(anyInt()))
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByPetOwnerId(anyInt());
    }

    @Test
    void getAllPetsThatHaveLeftShelterTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByLeaveTrue()).thenReturn(pets);
        assertThat(out.getAllPetsThatHaveLeftShelter())
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByLeaveTrue();
    }

    @Test
    void getAllAvailableTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByLeaveFalseAndPetOwnerIsNull()).thenReturn(pets);
        assertThat(out.getAllAvailable())
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByLeaveFalseAndPetOwnerIsNull();
    }

    @Test
    void gatAllOnProbationTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByLeaveFalseAndPetOwnerIsNotNull()).thenReturn(pets);
        assertThat(out.gatAllOnProbation())
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByLeaveFalseAndPetOwnerIsNotNull();
    }

    @Test
    void getAllAvailablePetsByTypeTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByTypeAndLeaveIsFalseAndPetOwnerIsNull(anyString())).thenReturn(pets);
        assertThat(out.getAllAvailablePetsByType(TypeOfPet.DOG))
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByTypeAndLeaveIsFalseAndPetOwnerIsNull(anyString());
    }

    @Test
    void getAllPetsOnProbationByTypeTest() {
        List<Pet> pets = List.of(testPet1, testPet2);
        when(petRepositoryMock.findAllByTypeAndLeaveIsFalseAndPetOwnerIsNotNull(anyString())).thenReturn(pets);
        assertThat(out.getAllPetsOnProbationByType(TypeOfPet.DOG))
                .isNotNull()
                .isEqualTo(pets);
        verify(petRepositoryMock, new Times(1)).findAllByTypeAndLeaveIsFalseAndPetOwnerIsNotNull(anyString());
    }

    @Test
    void findPetOnProbationByPetOwnerIdTest() {
        when(petRepositoryMock.getFirstByPetOwnerIdAndLeaveIsFalse(anyInt())).thenReturn(testPet1);
        assertThat(out.findPetOnProbationByPetOwnerId(anyInt()))
                .isNotNull()
                .isEqualTo(testPet1);
        verify(petRepositoryMock, new Times(1)).getFirstByPetOwnerIdAndLeaveIsFalse(anyInt());
    }
}