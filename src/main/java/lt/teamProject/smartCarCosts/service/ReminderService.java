package lt.teamProject.smartCarCosts.service;

import jakarta.transaction.Transactional;
import lt.teamProject.smartCarCosts.dto.ReminderRequest;
import lt.teamProject.smartCarCosts.entity.Reminder;
import lt.teamProject.smartCarCosts.entity.ReminderType;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.ReminderRepository;
import lt.teamProject.smartCarCosts.repository.ReminderTypeRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderTypeRepository reminderTypeRepository;
    private final UserCarRepository userCarRepository;
    private final UserService userService;
    private final EmailService emailService;

    public ReminderService(ReminderRepository reminderRepository,
                           ReminderTypeRepository reminderTypeRepository,
                           UserCarRepository userCarRepository,
                           UserService userService,
                           EmailService emailService) {
        this.reminderRepository = reminderRepository;
        this.reminderTypeRepository = reminderTypeRepository;
        this.userCarRepository = userCarRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Transactional
    public void createReminder(Long userId, ReminderRequest request) {

        UserCar userCar = userCarRepository.findByUserIdAndCarId(userId, request.getCarId())
                .orElseThrow(() -> new RuntimeException("Selected car does not belong to user"));

        ReminderType reminderType = reminderTypeRepository.findById(request.getReminderTypeId())
                .orElseThrow(() -> new RuntimeException("Reminder type not found"));

        if (request.isMonthBefore()) {
            saveReminder(userCar.getId(), request.getReminderTypeId(), request.getReminderDate(), 30);
        }

        if (request.isWeekBefore()) {
            saveReminder(userCar.getId(), request.getReminderTypeId(), request.getReminderDate(), 7);
        }

        if (request.isDayBefore()) {
            saveReminder(userCar.getId(), request.getReminderTypeId(), request.getReminderDate(), 1);
        }

        User user = userService.getUserById(userId);

        emailService.sendReminderCreatedEmail(
                user.getEmail(),
                reminderType.getName(),
                request.getReminderDate()
        );
    }

    private void saveReminder(Long userCarId, Long reminderTypeId, LocalDate endDate, int daysBefore) {
        Reminder reminder = new Reminder();
        reminder.setUserCarId(userCarId);
        reminder.setReminderTypeId(reminderTypeId);
        reminder.setNotifyBeforeDays(daysBefore);
        reminder.setRemindAt(endDate.minusDays(daysBefore));
        reminder.setActive(true);

        reminderRepository.save(reminder);
    }
}