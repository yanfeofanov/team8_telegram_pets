package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.DailyReportRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class DailyReportService {

    private final DailyReportRepository dailyReportRepository;

    private final PetOwnerService petOwnerService;

    private final PhotoService photoService;

    private final PetService petService;

    private final VolunteerService volunteerService;

    @Value("path.to.photo.dir")
    private String coversDir;

    @Autowired
    TelegramBot telegramBot;

    public DailyReportService(DailyReportRepository dailyReportRepository,
                              PetOwnerService petOwnerService,
                              PhotoService photoService,
                              PetService petService,
                              VolunteerService volunteerService) {
        this.dailyReportRepository = dailyReportRepository;
        this.petOwnerService = petOwnerService;
        this.photoService = photoService;
        this.petService = petService;
        this.volunteerService = volunteerService;
    }

    public DailyReport sendReport(Long chatId, String caption, PhotoSize[] photoSizes) throws IOException {
        DailyReport dailyReport = null;
        try {
            dailyReport = getNewReport(chatId, photoSizes, caption);
        } catch (IllegalArgumentException e) { // владелей не найден по айди или чат айди

        }

        return dailyReportRepository.save(dailyReport);
    }

    /**
     * Метод создает объекты типа PhotoPet и KeepingPet
     * Отправляет эти объекты в базу данных
     * Сохраняет фотографию питомца на сервер в папку
     * @param chatId Идентификатор чата
     * @param photoSizes массив объектов класса PhotoSize (фотографии, отправленные пользователем)
     * @param text Текстовое описание к фотографии
     * @return DailyReport сохраненный в БД отчет
     * @throws IOException
     */
    private DailyReport getNewReport(Long chatId, PhotoSize[] photoSizes, String text) throws IOException {
        PhotoSize photo = photoSizes[1];
        String fileId = photo.fileId();

        GetFile fileRequest = new GetFile(fileId);
        GetFileResponse fileResponse = telegramBot.execute(fileRequest);
        File file = fileResponse.file();
        byte[] fileData = telegramBot.getFileContent(file);

        Path filePath = Path.of(coversDir, fileId + "." +getExtension(file.filePath()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        long reportId = findReportIdByChatId(chatId);

        Photo photo1;
        if (reportId == -1) { // пользователь сегодня еще не отправлял отчет
            photo1 = createPhotoPet(chatId, fileRequest, file, filePath);
            photoService.savePhotoReport(photo1);
            uploadPhotoToServer(fileData, filePath);

            return createDailyReport(chatId, text, photo1);
        }
        else { // пользователь сегодня уже отправлял отчет
            Photo deletePhotoPet = dailyReportRepository.findDailyReportById(reportId).getPhoto();
            Files.deleteIfExists(Path.of(deletePhotoPet.getFilePath()));

            photo1 = updatePhotoPet(reportId, fileRequest, file, filePath);
            photoService.savePhotoReport(photo1);
            uploadPhotoToServer(fileData, filePath);

            return updateDailyReport(reportId, text, photo1);
        }
    }

    /**
     * Метод для поиска айди отчета, отправленного владельцем питомца сегодня
     * @param chatId идентификатор чата
     * @return айди искомого отчета
     */
    private long findReportIdByChatId(Long chatId) {
        PetOwner petOwner = petOwnerService.findPetOwnerWithProbationaryPeriod(chatId);
        List<DailyReport> reportsToday = (List<DailyReport>) getAllDailyReport(LocalDate.now());
        long reportId = -1; // идентификатор  отчета, отправленного пользователем сегодня
        if (petOwner != null) {
            for (DailyReport dailyReport : reportsToday) {
                if (dailyReport.getPetOwner().equals(petOwner)) {
                    reportId = dailyReport.getId();
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException("Владельца с таким chatId не существует:" + chatId);
        }
        return reportId;
    }

    /**
     * Метод создает объект типа PhotoPet
     *
     * @param chatId Идентификатор чата
     * @param fileRequest объект класса GetFile
     * @param file объект класса File
     * @param filePath путь к файлу
     * @return созданный объект класса PhotoPet
     */
    private Photo createPhotoPet(Long chatId, GetFile fileRequest, File file, Path filePath) {

        Photo photoPet = new Photo();
        photoPet.setMediaType(fileRequest.getContentType());
        photoPet.setFileSize(file.fileSize());
        photoPet.setFilePath(filePath.toString());
//
//        PetOwner petOwner = petOwnerService.findPetOwnerWithProbationaryPeriod(chatId);
//        if (petOwner != null) {
//            petOwner.g();
//        }else {
//            throw new IllegalArgumentException("Владельца с таким chatId не существует:" + chatId);
//        }

        return photoPet;
    }
    /**
     * Метод обновляет объект типа PhotoPet
     *
     * @param reportId айди отчета о питомце
     * @param fileRequest объект класса GetFile
     * @param file объект класса File
     * @param filePath путь к файлу
     * @return созданный объект класса PhotoPet
     */
    private Photo updatePhotoPet(Long reportId, GetFile fileRequest, File file, Path filePath) {

        DailyReport dailyReport = dailyReportRepository.findDailyReportById(reportId);

        Photo photoPet = dailyReport.getPhoto();

        photoPet.setMediaType(fileRequest.getContentType());
        photoPet.setFileSize(file.fileSize());
        photoPet.setFilePath(filePath.toString());

        return photoPet;

    }

    /**
     * /метод создает новый отчет о питомце
     * @param chatId идентификатор чата
     * @param text текстовое сообщение к фотографии
     * @param photo объект содержащий информацию. о фотографии
     * @return новый отчет
     */
    private DailyReport createDailyReport(Long chatId, String text, Photo photo) {
        PetOwner petOwner = petOwnerService.findPetOwnerWithProbationaryPeriod(chatId);
        Pet pet = petService.findPet(petOwner.getId());
        Volunteer volunteer = volunteerService.getRandomVolunteer();

        DailyReport dailyReport = new DailyReport();
        if (petOwner != null) {
            dailyReport.setPetOwner(petOwner);

        } else {
            throw new IllegalArgumentException("Владельца с таким chatId не существует:" + chatId);
        }
        dailyReport.setDate(LocalDateTime.now());
        dailyReport.setReportBody(text);
        dailyReport.setPhoto(photo);
        dailyReport.setPet(pet);
        dailyReport.setInspector(volunteer);
        return dailyReport;
    }
    /**
     * Метод обновляет отчет о питомце.
     * За один день владелец питомца может отправить в БД только один отчет.
     * Если владелец отправляет 2-ой или более отчет в день, то текущий отчет обновляется.
     * @param reportId айди текущего отчета
     * @param caption новый текстовый отчет
     * @param photo новое фотография питомца
     * @return обновленный отчет
     */
    private DailyReport updateDailyReport(long reportId, String caption, Photo photo) {
        DailyReport dailyReport = dailyReportRepository.findDailyReportById(reportId);

        dailyReport.setDate(LocalDateTime.now());
        dailyReport.setReportBody(caption);
        dailyReport.setPhoto(photo);
        return dailyReport;
    }

    /**
     * Метод загружает фотографию питомца на сервер в папку
     * @param fileData массив байтов, хранящий фотографию
     * @param filePath путь для сохранения фотографии
     * @Throw RuntimeException ошибка при сохранении фото
     *
     */
    private void uploadPhotoToServer(byte[] fileData, Path filePath) {
        try (InputStream is = new ByteArrayInputStream(fileData);
             OutputStream os=Files.newOutputStream(filePath,CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * первая стадия метода отправки отчета. Этот метод отпрвляет пользователю сообщение с просьбой
     * отправить отчет: текст и фото
     *
     * @param chatId      идентификатор чата
     * @param messageText сообщение для пользователя
     */
    public void sendReport(long chatId, String messageText) {
        sendMessageReply(chatId, messageText);
    }

    /**
     * Метод вызывается при отправке отчета пользователем, который
     * не является владельцем питомца
     * @param chatId идентификатор чата
     * @param messageText сообщение пользователю
     */
    public void sendReportWithoutReply(long chatId, String messageText) {
        sendMessage(chatId, messageText);
    }

    /**
     * Метод получает расширение файла из его полного пути
     * @param fileName имя файла
     * @return расширение файла
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void sendMessageReply(long chatId, String messageText) {
        SendMessage sendMess = new SendMessage(chatId, messageText);
        sendMess.replyMarkup(new ForceReply());
        telegramBot.execute(sendMess);
    }

    public void sendMessage(long chatId, String messageText) {
        SendMessage sendMess = new SendMessage(chatId, messageText);
        telegramBot.execute(sendMess);
    }

    /**
     * Метод выводит список всех отчетов по определенным датам.
     *
     * @return Collection
     */
    public Collection<DailyReport> getAllDailyReport(LocalDate date){
        return dailyReportRepository.findDailyReportByDateBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

}
