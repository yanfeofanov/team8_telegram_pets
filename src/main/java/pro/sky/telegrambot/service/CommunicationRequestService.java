package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.CommunicationRequestNotFoundException;
import pro.sky.telegrambot.model.CommunicationRequest;
import pro.sky.telegrambot.model.User;
import pro.sky.telegrambot.repository.CommunicationRequestRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TimeZone;

@Service
public class CommunicationRequestService {

    private final CommunicationRequestRepository communicationRequestRepository;
    private final UserService userService;
    private final VolunteerService volunteerService;

    public CommunicationRequestService(CommunicationRequestRepository communicationRequestRepository, UserService userService, VolunteerService volunteerService) {
        this.communicationRequestRepository = communicationRequestRepository;
        this.userService = userService;
        this.volunteerService = volunteerService;
    }

    public CommunicationRequest addRequestToDatabase(Long userId, String contactInfo) {
        User user = userService.findUserById(userId);
        if (user != null) {
            CommunicationRequest communicationRequest = communicationRequestRepository.findByUserAndDoneIsFalse(user);
            if (communicationRequest == null) {
                return communicationRequestRepository.save(new CommunicationRequest(
                        LocalDateTime.now(TimeZone.getTimeZone("GMT+3").toZoneId()),
                        user,
                        contactInfo,
                        Boolean.FALSE,
                        volunteerService.getRandomVolunteer()));
            } else {
                communicationRequest.setContactInfo(contactInfo);
                return communicationRequestRepository.save(communicationRequest);
            }
        }
        return null;
    }

    public Collection<CommunicationRequest> getAllRequestForPeriodByDone(LocalDateTime startPeriod, LocalDateTime endPeriod, boolean done) {
        return communicationRequestRepository.findByDateBetweenAndDone(startPeriod, endPeriod, done);
    }

    public CommunicationRequest checkCommunicationRequest(int id, boolean done) {
        CommunicationRequest request = communicationRequestRepository.findById(id).orElseThrow(CommunicationRequestNotFoundException::new);
        request.setDone(done);
        return communicationRequestRepository.save(request);
    }
}