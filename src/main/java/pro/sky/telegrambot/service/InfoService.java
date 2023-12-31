package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.TypesOfInformation;
import pro.sky.telegrambot.exception.BadParamException;
import pro.sky.telegrambot.exception.InfoNotFoundException;
import pro.sky.telegrambot.exception.ShelterNotFoundException;
import pro.sky.telegrambot.model.Info;
import pro.sky.telegrambot.model.Shelter;
import pro.sky.telegrambot.repository.InfoRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Добавление новой информации
     *
     * @param info;
     * @return Добавление информации по приюту
     */
    public Info addInfo(Info info) {
        return infoRepository.save(info);
    }

    /**
     * Поиск информации о приюте по его ID
     *
     * @param id;
     * @return Информацию о приюте
     */
    public Collection<Info> findByShelterIdInfo(int id) {
        //Проверка на несуществующие приюты
        if (infoRepository.findByShelterId(id).isEmpty()) {
            throw new ShelterNotFoundException("Информация с "+id+" не найдена!");
        }
        return infoRepository.findByShelterId(id);
    }


    public Info updateInfo(Info info){
        if(infoRepository.existsById(info.getId())){
            return infoRepository.save(info);
        }
            else throw new InfoNotFoundException("Информация с "+info.getId()+" не найдена!");
    }

}