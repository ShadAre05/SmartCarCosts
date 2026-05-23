package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.dto.ReminderRequest;
import lt.teamProject.smartCarCosts.entity.Reminder;
import lt.teamProject.smartCarCosts.entity.ReminderType;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.entity.UserCar;
import lt.teamProject.smartCarCosts.repository.ReminderRepository;
import lt.teamProject.smartCarCosts.repository.ReminderTypeRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;
    @Mock
    private ReminderTypeRepository reminderTypeRepository;
    @Mock
    private UserCarRepository userCarRepository;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReminderService reminderService;

    @Captor
    private ArgumentCaptor<Reminder> reminderCaptor;

    @Test
    void createReminder_ShouldSaveMultipleRemindersAndSendEmail() {
        Long userId = 1L;
        Long carId = 10L;
        Long typeId = 5L;
        LocalDate eventDate = LocalDate.of(2026, 12, 31);

        ReminderRequest request = new ReminderRequest();
        ReflectionTestUtils.setField(request, "carId", carId);
        ReflectionTestUtils.setField(request, "reminderTypeId", typeId);
        ReflectionTestUtils.setField(request, "reminderDate", eventDate);
        ReflectionTestUtils.setField(request, "monthBefore", true);
        ReflectionTestUtils.setField(request, "weekBefore", false);
        ReflectionTestUtils.setField(request, "dayBefore", true);

        UserCar userCar = new UserCar();
        ReflectionTestUtils.setField(userCar, "id", 100L);

        ReminderType type = new ReminderType();
        ReflectionTestUtils.setField(type, "name", "Insurance");

        User user = new User();
        ReflectionTestUtils.setField(user, "email", "driver@test.com");

        when(userCarRepository.findByUserIdAndCarId(userId, carId)).thenReturn(Optional.of(userCar));
        when(reminderTypeRepository.findById(typeId)).thenReturn(Optional.of(type));
        when(userService.getUserById(userId)).thenReturn(user);

        reminderService.createReminder(userId, request);

        verify(reminderRepository, times(2)).save(reminderCaptor.capture());

        var savedReminders = reminderCaptor.getAllValues();
        assertEquals(30, savedReminders.get(0).getNotifyBeforeDays());
        assertEquals(LocalDate.of(2026, 12, 1), savedReminders.get(0).getRemindAt());

        assertEquals(1, savedReminders.get(1).getNotifyBeforeDays());
        assertEquals(LocalDate.of(2026, 12, 30), savedReminders.get(1).getRemindAt());

        verify(emailService, times(1)).sendReminderCreatedEmail("driver@test.com", "Insurance", eventDate);
    }

    @Test
    void createReminder_WhenCarDoesNotBelongToUser_ShouldThrowException() {
        Long userId = 1L;
        ReminderRequest request = new ReminderRequest();
        ReflectionTestUtils.setField(request, "carId", 99L);

        when(userCarRepository.findByUserIdAndCarId(userId, 99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reminderService.createReminder(userId, request);
        });

        assertEquals("Selected car does not belong to user", exception.getMessage());

        verify(reminderRepository, never()).save(any());
        verify(emailService, never()).sendReminderCreatedEmail(any(), any(), any());
    }

    @Test
    void deleteReminder_WhenAccessDenied_ShouldThrowException() {
        Long hackerId = 2L;
        Long reminderId = 55L;

        Reminder reminder = new Reminder();
        ReflectionTestUtils.setField(reminder, "userCarId", 100L);

        UserCar targetCar = new UserCar();
        ReflectionTestUtils.setField(targetCar, "userId", 1L);

        when(reminderRepository.findById(reminderId)).thenReturn(Optional.of(reminder));
        when(userCarRepository.findById(100L)).thenReturn(Optional.of(targetCar));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reminderService.deleteReminder(hackerId, reminderId);
        });

        assertEquals("Access denied", exception.getMessage());
        verify(reminderRepository, never()).deleteById(anyLong());
    }
}