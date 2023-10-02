package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.constant.TypeOfPet;

import static org.assertj.core.api.Assertions.assertThat;

class KeyboardServiceTest {
    private final KeyboardService out = new KeyboardService();

    @Test
    void generateShelterSelectionMenuTest() {
        InlineKeyboardMarkup result = out.generateShelterSelectionMenu();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void generateMenuPreparingForAdoptionTest() {
        for (TypeOfPet value : TypeOfPet.values()) {
            assertThat(out.generateMenuPreparingForAdoption(value))
                    .isNotNull()
                    .isInstanceOf(InlineKeyboardMarkup.class);
        }
    }

    @Test
    void generateMainDogShelterMenuTest() {
        assertThat(out.generateMainDogShelterMenu())
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
    }

    @Test
    void generateInfoDogShelterMenuTest() {
        assertThat(out.generateInfoDogShelterMenu())
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
    }

    @Test
    void generateMainCatShelterMenuTest() {
        assertThat(out.generateMainCatShelterMenu())
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
    }

    @Test
    void generateInfoCatShelterMenuTest() {
        assertThat(out.generateInfoCatShelterMenu())
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
    }

    @Test
    void generateCommunicationOptionMenuTest() {
        InlineKeyboardMarkup result = out.generateShelterSelectionMenu();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void backButtonMenuDogTest() {
        InlineKeyboardMarkup result = out.backButtonMenuDog();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void backButtonMenuCatTest() {
        InlineKeyboardMarkup result = out.backButtonMenuCat();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void backCatInfoMenuTest() {
        InlineKeyboardMarkup result = out.backCatInfoMenu();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void backDogInfoMenuTest() {
        InlineKeyboardMarkup result = out.backDogInfoMenu();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void backDogAdoptionMenuTest() {
        InlineKeyboardMarkup result = out.backDogAdoptionMenu();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }

    @Test
    void backCatAdoptionMenuTest() {
        InlineKeyboardMarkup result = out.backCatAdoptionMenu();
        assertThat(result)
                .isNotNull()
                .isInstanceOf(InlineKeyboardMarkup.class);
        assertThat(result.inlineKeyboard().length).isEqualTo(1);
    }
}