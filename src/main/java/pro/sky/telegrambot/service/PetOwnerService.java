package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.ProbationStatus;
import pro.sky.telegrambot.exception.ErrorCollisionException;
import pro.sky.telegrambot.exception.InvalidInputDataException;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.DailyReportRepository;
import pro.sky.telegrambot.repository.PetOwnerRepository;
import pro.sky.telegrambot.repository.PetRepository;
import pro.sky.telegrambot.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pro.sky.telegrambot.constant.TypeOfPet.CAT;
import static pro.sky.telegrambot.constant.TypeOfPet.DOG;

@Service
public class PetOwnerService {

    private final PetOwnerRepository petOwnerRepository;

    private final TelegramBot telegramBot;

    private final PetRepository petRepository;

    private final DailyReportRepository dailyReportRepository;

    private final TelegramBotService telegramBotService;

    private final VolunteerService volunteerService;

    public PetOwnerService(PetOwnerRepository petOwnerRepository, TelegramBot telegramBot, PetRepository petRepository,
                           DailyReportRepository dailyReportRepository, TelegramBotService telegramBotService,
                           VolunteerService volunteerService) {
        this.petOwnerRepository = petOwnerRepository;
        this.telegramBot = telegramBot;
        this.petRepository = petRepository;
        this.dailyReportRepository = dailyReportRepository;
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
        HashMap<PetOwner, Collection<DailyReport>> map = new HashMap<>();
        for (PetOwner petOwner : petOwners) {
            Collection<DailyReport> dailyReports = dailyReportRepository.findDailyReportByPetOwner(petOwner);
            map.put(petOwner, dailyReports);
        }
        for (Map.Entry<PetOwner, Collection<DailyReport>> entry : map.entrySet()) {
            PetOwner key = entry.getKey();
            Collection<DailyReport> value = entry.getValue();
            long quantityApproved = value.stream().filter(DailyReport::getApproved).count();
            long size = value.size();
            if ((quantityApproved / size) * 100 >= 80 && value.stream().skip(size - 7)
                    .anyMatch(DailyReport::getApproved)) {
                key.setProbation(false);
                telegramBotService.sendReply(key.getUser().getChatId(),
                        "Поздравляем, вы успешно прошли испытательный срок, отправлять отчет больше не требуется!");
            } else if (value.size() >= 58 && value.stream().skip(size - 7)
                    .anyMatch(dailyReport -> !dailyReport.getApproved())) {
                telegramBotService.sendReply(key.getUser().getChatId(),
                        "К сожалению, вы не прошли испытательный срок, с Вами скоро свяжется волонтер для дальнейших инструкций");
            } else {
                Volunteer volunteer = volunteerService.getRandomVolunteer();
                telegramBotService.sendReply(volunteer.getUser().getChatId(),
                        "Необходимо принять решение о статусе усыновителя " + key.getUser().getId(), generateMenu());


            }
        }
    }

    public Keyboard generateMenu() {
        List<ProbationStatus> probationStatuses = List.of(ProbationStatus.SUCCESS, ProbationStatus.SHORT_STATUS,
                ProbationStatus.LONG_STATUS);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        for (ProbationStatus probationStatus : probationStatuses) {
            InlineKeyboardButton inlineButton = new InlineKeyboardButton(probationStatus.getText());
            inlineButton.callbackData(probationStatus.getText());
            keyboard.addRow(inlineButton);
        }
        return keyboard;
    }



}
