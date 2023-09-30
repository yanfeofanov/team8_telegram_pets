package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.ErrorCollisionException;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.PetOwnerRepository;
import pro.sky.telegrambot.repository.PetRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import static pro.sky.telegrambot.constant.TypeOfPet.CAT;
import static pro.sky.telegrambot.constant.TypeOfPet.DOG;

@Service
public class PetOwnerService {

    private final PetOwnerRepository petOwnerRepository;

    private final PetRepository petRepository;


    private final TelegramBotService telegramBotService;

    private final VolunteerService volunteerService;

    public PetOwnerService(PetOwnerRepository petOwnerRepository, PetRepository petRepository,
                           TelegramBotService telegramBotService, VolunteerService volunteerService) {
        this.petOwnerRepository = petOwnerRepository;
        this.petRepository = petRepository;
        this.telegramBotService = telegramBotService;
        this.volunteerService = volunteerService;
    }


    public PetOwner findPetOwnerWithProbationaryPeriod(Long userId) {
        return petOwnerRepository.findByUserIdAndProbationIsTrue(userId);
    }

    public PetOwner findPetOwnerByPhone(String phoneNumber) {
        PetOwner foundOwner = petOwnerRepository.findPetOwnerByPhoneNumber(phoneNumber);
        if (!phoneNumber.isBlank() ||
                phoneNumber.equals(foundOwner.getPhoneNumber())) {
            return foundOwner;
        } else {
            throw new InvalidInputDataException("Проверьте корректность входных данных");
        }
    }

    public PetOwner addPetOwner(PetOwner newPetOwner) {
        boolean ownerExist = petOwnerRepository.findAll().stream()
                .anyMatch(petOwner -> petOwner.equals(newPetOwner));
        boolean userExist = petOwnerRepository.findAll().stream()
                .map(PetOwner::getUser)
                .anyMatch(user -> user.getId().equals(newPetOwner.getUser().getId()));
        if (ownerExist || userExist) {
            throw new ErrorCollisionException("Создание дубликата недопустимо, усыновитель или пользователь с таким id уже существует");
        }
        return petOwnerRepository.save(newPetOwner);
    }

    public PetOwner deletePetOwnerById(int id) {
        PetOwner deletePetOwner = petOwnerRepository.findById(id)
                .orElseThrow(() -> new ErrorCollisionException("Усыновитель не найден"));
        petOwnerRepository.delete(deletePetOwner);
        return deletePetOwner;
    }

    public Collection<PetOwner> getCatOwners() {
        return petRepository.findAll().stream()
                .filter(pet -> pet.getShelter().getType().equals(CAT))
                .map(Pet::getPetOwner)
                .collect(Collectors.toList());
    }

    public Collection<PetOwner> getDogOwners() {
        return petRepository.findAll().stream()
                .filter(pet -> pet.getShelter().getType().equals(DOG))
                .map(Pet::getPetOwner)
                .collect(Collectors.toList());
    }

    public Collection<PetOwner> getPetOwnersOnProbation() {
        return petOwnerRepository.findAll().stream()
                .filter(PetOwner::isProbation)
                .collect(Collectors.toList());
    }


    @Scheduled(cron = "0 0 12 * * ?")
    public void checkProbation() {
        LocalDate localDate = LocalDate.now();
        List<PetOwner> petOwners = petOwnerRepository.findAll().stream()
                .filter(petOwner -> petOwner.getEndProbation().toLocalDate().isEqual(localDate))
                .collect(Collectors.toList());
        if (!petOwners.isEmpty()) {
            Volunteer volunteer = volunteerService.getRandomVolunteer();
            telegramBotService.sendReply(volunteer.getUser().getChatId(),
                    "Необходимо принять решение о статусе следующих усыновителей ", generateMenu(petOwners));

        }
    }

    public InlineKeyboardMarkup generateMenu(List<PetOwner> petOwners) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        for (PetOwner petOwner : petOwners) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(petOwner.getId() + " " + petOwner.getName() +
                    " " + petOwner.getSurname());
            inlineButton.callbackData(petOwner.getId() + " " + petOwner.getName() +
                    " " + petOwner.getSurname());
            keyboard.addRow(inlineButton);
        }
        return keyboard;
    }

    public PetOwner changeProbationStatus(int ownerId, int status) {
        LocalDateTime localDateTime = LocalDateTime.now();
        PetOwner petOwner = petOwnerRepository.getById(ownerId);
        if (petOwner == null) {
            throw new InvalidInputDataException("Усыновитель не найден, перепроверьте id");
        } else {
            long chatId = petOwner.getUser().getChatId();
            switch (status) {
                case 0:
                    petOwner.setProbation(false);
                    telegramBotService.sendReply(chatId, "Поздравляем, испытательный срок пройден");
                    break;
                case 1:
                    petOwner.setEndProbation(localDateTime.plusDays(14));
                    telegramBotService.sendReply(chatId, "Сообщаем, что Ваш испытательный срок продлен на 14 дней");
                    break;
                case 2:
                    petOwner.setEndProbation(localDateTime.plusDays(30));
                    telegramBotService.sendReply(chatId, "Сообщаем, что Ваш испытательный срок продлен на 30 дней");
                    break;
                case 3:
                    petOwner.setProbation(false);
                    petOwner.setEndProbation(null);
                    telegramBotService.sendReply(chatId, "К сожалению, вы не прошли испытательный срок, " +
                            "с Вами скоро свяжется волонтер для дальнейших указаний");
                    break;
                default:
                    throw new InvalidInputDataException("Значения статусов должно быть от 0 до 3");
            }
            return petOwner;
        }
    }

}
