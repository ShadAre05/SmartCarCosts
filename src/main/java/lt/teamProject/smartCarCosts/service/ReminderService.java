package lt.teamProject.smartCarCosts.service;

import jakarta.transaction.Transactional;
import lt.teamProject.smartCarCosts.dto.ReminderOverviewRow;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<ReminderOverviewRow> getUserReminderOverview(Long userId) {

        List<UserCar> userCars = userCarRepository.findByUserId(userId);

        List<Long> userCarIds = userCars.stream()
                .map(UserCar::getId)
                .toList();

        if (userCarIds.isEmpty()) {
            return List.of();
        }

        List<Reminder> reminders = reminderRepository
                .findByUserCarIdInAndActiveTrueOrderByRemindAtAsc(userCarIds);

        Map<String, List<Reminder>> groupedReminders = reminders.stream()
                .collect(Collectors.groupingBy(reminder -> {
                    LocalDate expiresOn = reminder.getRemindAt()
                            .plusDays(reminder.getNotifyBeforeDays());

                    return reminder.getUserCarId()
                            + "-"
                            + reminder.getReminderTypeId()
                            + "-"
                            + expiresOn;
                }));

        return groupedReminders.values()
                .stream()
                .map(group -> {
                    Reminder first = group.get(0);

                    ReminderType type = reminderTypeRepository
                            .findById(first.getReminderTypeId())
                            .orElse(null);

                    String typeName = type != null ? type.getName() : "Reminder";

                    LocalDate expiresOn = first.getRemindAt()
                            .plusDays(first.getNotifyBeforeDays());

                    List<Long> ids = group.stream()
                            .map(Reminder::getId)
                            .toList();

                    boolean monthBefore = group.stream()
                            .anyMatch(r -> r.getNotifyBeforeDays() == 30);

                    boolean weekBefore = group.stream()
                            .anyMatch(r -> r.getNotifyBeforeDays() == 7);

                    boolean dayBefore = group.stream()
                            .anyMatch(r -> r.getNotifyBeforeDays() == 1);

                    return new ReminderOverviewRow(
                            ids,
                            typeName,
                            expiresOn,
                            monthBefore,
                            weekBefore,
                            dayBefore
                    );
                })
                .toList();
    }

    @Transactional
    public void deleteReminderGroup(Long userId, List<Long> reminderIds) {

        for (Long reminderId : reminderIds) {
            deleteReminder(userId, reminderId);
        }
    }

    @Transactional
    public void deleteReminder(Long userId, Long reminderId) {

        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        UserCar userCar = userCarRepository.findById(reminder.getUserCarId())
                .orElseThrow(() -> new RuntimeException("User car not found"));

        if (!userCar.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        reminderRepository.deleteById(reminderId);
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