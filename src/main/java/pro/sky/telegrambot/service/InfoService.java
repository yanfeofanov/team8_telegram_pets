package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.InfoRepository;

@Service
public class InfoService {
    private final InfoRepository infoRepository;

    public InfoService(InfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    public Info findByTypeAndShelter(TypesOfInformation typesOfInformation, Shelter shelter) {
            return infoRepository.findByTypeAndShelter(typesOfInformation, shelter);
    }

    public Info findByType(TypesOfInformation typesOfInformation) {
        return infoRepository.findByType(typesOfInformation);
    }
}
