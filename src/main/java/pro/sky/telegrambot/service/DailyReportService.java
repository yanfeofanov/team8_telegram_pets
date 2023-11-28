package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.DailyReportNullPointerException;
import pro.sky.telegrambot.exception.PetOwnerNullPointerException;
import pro.sky.telegrambot.model.*;
import pro.sky.telegrambot.repository.DailyReportRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TimeZone;

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

    private final TelegramBot telegramBot;

    public DailyReportService(DailyReportRepository dailyReportRepository,
                              PetOwnerService petOwnerService,
                              PhotoService photoService,
                              PetService petService,
                              VolunteerService volunteerService, TelegramBot telegramBot) {
        this.dailyReportRepository = dailyReportRepository;
        this.petOwnerService = petOwnerService;
        this.photoService = photoService;
        this.petService = petService;
        this.volunteerService = volunteerService;
        this.telegramBot = telegramBot;
    }

    public DailyReport sendReport(Long userId, String caption, PhotoSize[] photoSizes) throws IOException {
        DailyReport dailyReport;
        dailyReport = getNewReport(userId, photoSizes, caption);
        return dailyReportRepository.save(dailyReport);
    }

    private DailyReport getNewReport(Long userId, PhotoSize[] photoSizes, String text) throws IOException {
        PhotoSize photo = photoSizes[1];
        String fileId = photo.fileId();

        GetFile fileRequest = new GetFile(fileId);
        GetFileResponse fileResponse = telegramBot.execute(fileRequest);
        File file = fileResponse.file();
        byte[] fileData = telegramBot.getFileContent(file);

        Path filePath = Path.of(coversDir, fileId + "." + getExtension(file.filePath()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        DailyReport report = findReportByUserId(userId);
        Photo photo1;
        if (report == null) { // пользователь сегодня еще не отправлял отчет
            photo1 = createPhotoPet(fileRequest, file, filePath);
            photoService.savePhotoReport(photo1);
            uploadPhotoToServer(fileData, filePath);

            return createDailyReport(userId, text, photo1);
        } else { // пользователь сегодня уже отправлял отчет
            Photo deletePhotoPet = report.getPhoto();
            Files.deleteIfExists(Path.of(deletePhotoPet.getFilePath()));

            photo1 = updatePhotoPet(report, fileRequest, file, filePath);
            photoService.savePhotoReport(photo1);
            uploadPhotoToServer(fileData, filePath);

            return updateDailyReport(report, text, photo1);
        }
    }

    /**
     * Метод для поиска айди отчета, отправленного владельцем питомца сегодня
     *
     * @param userId идентификатор чата
     * @return айди искомого отчета
     */
    public DailyReport findReportByUserId(Long userId) {
        PetOwner petOwner = petOwnerService.findPetOwnerWithProbationaryPeriod(userId);
        return getTodayDailyReportByPetOwner(petOwner, LocalDate.now(TimeZone.getTimeZone("GMT+3").toZoneId()));
    }

    /**
     * Метод создает объект типа PhotoPet
     *
     * @param fileRequest объект класса GetFile
     * @param file        объект класса File
     * @param filePath    путь к файлу
     * @return созданный объект класса PhotoPet
     */
    private Photo createPhotoPet(GetFile fileRequest, File file, Path filePath) {

        Photo photoPet = new Photo();
        photoPet.setMediaType(fileRequest.getContentType());
        photoPet.setFileSize(file.fileSize());
        photoPet.setFilePath(filePath.toString());
        photoPet.setDate(LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId()));
        return photoPet;
    }

    /**
     * Метод обновляет объект типа PhotoPet
     *
     * @param dailyReport отчет о питомце
     * @param fileRequest объект класса GetFile
     * @param file        объект класса File
     * @param filePath    путь к файлу
     * @return созданный объект класса PhotoPet
     */
    private Photo updatePhotoPet(DailyReport dailyReport, GetFile fileRequest, File file, Path filePath) {
        Photo photoPet = dailyReport.getPhoto();
        photoPet.setMediaType(fileRequest.getContentType());
        photoPet.setFileSize(file.fileSize());
        photoPet.setFilePath(filePath.toString());

        return photoPet;
    }

    /**
     * /метод создает новый отчет о питомце
     *
     * @param userId идентификатор пользователя бота
     * @param text   текстовое сообщение к фотографии
     * @param photo  объект содержащий информацию. о фотографии
     * @return новый отчет
     */
    public DailyReport createDailyReport(Long userId, String text, Photo photo) {
        PetOwner petOwner = petOwnerService.findPetOwnerWithProbationaryPeriod(userId);
        if (petOwner == null) {
            throw new PetOwnerNullPointerException(userId);
        }
        Pet pet = petService.findPetOnProbationByPetOwnerId(petOwner.getId());
        Volunteer volunteer = volunteerService.getRandomVolunteer();

        DailyReport dailyReport = new DailyReport();
        dailyReport.setPetOwner(petOwner);
        dailyReport.setDate(LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId()));
        dailyReport.setReportBody(text);
        dailyReport.setPhoto(photo);
        dailyReport.setPet(pet);
        dailyReport.setChecked(false);
        dailyReport.setInspector(volunteer);
        dailyReport.setApproved(false);
        return dailyReport;
    }

    /**
     * Метод обновляет отчет о питомце.
     * За один день владелец питомца может отправить в БД только один отчет.
     * Если владелец отправляет 2-ой или более отчет в день, то текущий отчет обновляется.
     *
     * @param dailyReport текущий отчет о питомце
     * @param caption     новый текстовый отчет
     * @param photo       новое фотография питомца
     * @return обновленный отчет
     */
    public DailyReport updateDailyReport(DailyReport dailyReport, String caption, Photo photo) {
        dailyReport.setDate(LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId()));
        dailyReport.setReportBody(caption);
        dailyReport.setPhoto(photo);
        return dailyReport;
    }

    /**
     * Метод загружает фотографию питомца на сервер в папку
     *
     * @param fileData массив байтов, хранящий фотографию
     * @param filePath путь для сохранения фотографии
     */
    private void uploadPhotoToServer(byte[] fileData, Path filePath) {
        try (InputStream is = new ByteArrayInputStream(fileData);
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод получает расширение файла из его полного пути
     *
     * @param fileName имя файла
     * @return расширение файла
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public DailyReport getTodayDailyReportByPetOwner(PetOwner petOwner, LocalDate date) {
        return dailyReportRepository.findByPetOwnerAndDateBetween(petOwner, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    /**
     * Метод выводит список проверенных/непроверенных отчетов
     *
     * @param check статус отчета
     * @return список отчетов
     */
    public Collection<DailyReport> getCheckedDailyReport(Boolean check) {
        return dailyReportRepository.findDailyReportByChecked(check);
    }

    /**
     * Метод выводит список отчетов владельца жиотных
     *
     * @param petOwnerId ID владельца животного
     * @return список отчетов
     */
    public Collection<DailyReport> getAllDailyReportByPetOwner(int petOwnerId) {
        PetOwner petOwner = petOwnerService.findPetOwner(petOwnerId);
        if (petOwner != null) {
            return dailyReportRepository.findDailyReportByPetOwner(petOwner);
        } else {
            throw new PetOwnerNullPointerException(petOwnerId);
        }
    }

    /**
     * Метод выводит отчет по ID
     *
     * @param dailyReportId ID отчета
     * @return список отчетов
     */
    public DailyReport getDailyReportById(Long dailyReportId) {
        DailyReport report = dailyReportRepository.findDailyReportById(dailyReportId);
        if (report != null) {
            return report;
        } else {
            throw new DailyReportNullPointerException(dailyReportId);
        }
    }

    /**
     * метод для волонтера, изменение статуса отчета
     *
     * @param id      - id владельца питомца
     * @param checked - статус отчета (проверен(true)/не проверен(false))
     */
    public String checkDailyReport(Long id, boolean checked) {
        DailyReport findDailyReport = dailyReportRepository.findDailyReportById(id);
        if (findDailyReport == null) {
            throw new DailyReportNullPointerException(id);
        }else if (checked == findDailyReport.getChecked()) {
            return "отчет уже проверен";
        }else {
        findDailyReport.setChecked(checked);
        dailyReportRepository.save(findDailyReport);
        return "статус отчета изменен";
        }
    }

    /**
     * Метод выводит список отчетов по дате
     *
     * @param date день за который нужно вывести отчеты
     * @return список отчетов
     */
    public Collection<DailyReport> getAllDailyReportByDate(String date) {
            LocalDate localDate = LocalDate.parse(date);
            Collection<DailyReport> reportsByDate = dailyReportRepository.
                    findDailyReportByDateBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());
            return reportsByDate;
    }
}


