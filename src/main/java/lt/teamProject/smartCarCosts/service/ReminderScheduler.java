package lt.teamProject.smartCarCosts.service;

import jakarta.transaction.Transactional;
import lt.teamProject.smartCarCosts.entity.Reminder;
import lt.teamProject.smartCarCosts.entity.ReminderType;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.ReminderRepository;
import lt.teamProject.smartCarCosts.repository.ReminderTypeRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final ReminderTypeRepository reminderTypeRepository;
    private final UserCarRepository userCarRepository;
    private final UserService userService;
    private final EmailService emailService;

    public ReminderScheduler(ReminderRepository reminderRepository,
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

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void sendTodayReminders() {
        LocalDate today = LocalDate.now();

        List<Reminder> reminders = reminderRepository.findByRemindAtAndActiveTrue(today);

        for (Reminder reminder : reminders) {
            UserCar userCar = userCarRepository.findById(reminder.getUserCarId())
                    .orElse(null);

            if (userCar == null) {
                reminder.setActive(false);
                continue;
            }

            User user = userService.getUserById(userCar.getUserId());

            ReminderType reminderType = reminderTypeRepository.findById(reminder.getReminderTypeId())
                    .orElse(null);

            String reminderTypeName = reminderType != null ? reminderType.getName() : "Reminder";

            emailService.sendReminderNotificationEmail(
                    user.getEmail(),
                    reminderTypeName,
                    reminder.getNotifyBeforeDays(),
                    reminder.getRemindAt()
            );
            reminder.setActive(false);
        }
    }
}
