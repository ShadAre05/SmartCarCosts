package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import lt.teamProject.smartCarCosts.dto.UpdateProfileRequest;
import lt.teamProject.smartCarCosts.entity.*;
import lt.teamProject.smartCarCosts.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CountryRepository countryRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private CurrencyRepository currencyRepository;
    @Mock private UserCarRepository userCarRepository;
    @Mock private ReminderRepository reminderRepository;
    @Mock private ExpenseRepository expenseRepository;
    @Mock private ConfirmationTokenRepository confirmationTokenRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void registerUser_ShouldAssignDefaultsAndDisableUser() {
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "email", "new@test.com");
        ReflectionTestUtils.setField(request, "password", "secret");
        ReflectionTestUtils.setField(request, "fullName", "John Doe");
        ReflectionTestUtils.setField(request, "country", "Lithuania");

        Country country = new Country();
        Role role = new Role();
        Currency currency = new Currency();

        when(countryRepository.findByCountryName("Lithuania")).thenReturn(Optional.of(country));
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(role));
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        userService.registerUser(request);

        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("new@test.com", savedUser.getEmail());
        assertFalse(savedUser.isEnabled(), "New user MUST BE denied before confirm his email");
        assertEquals(country, savedUser.getCountry());
        assertEquals(role, savedUser.getRole());
        assertEquals(currency, savedUser.getCurrency());
    }

    @Test
    void deleteUser_ShouldCascadeDeleteEverything() {
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "email", "delete_me@test.com");

        UserCar car1 = new UserCar(); ReflectionTestUtils.setField(car1, "id", 10L);
        UserCar car2 = new UserCar(); ReflectionTestUtils.setField(car2, "id", 20L);
        List<Long> carIds = List.of(10L, 20L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userCarRepository.findByUserId(userId)).thenReturn(List.of(car1, car2));

        userService.deleteUser(userId);

        verify(reminderRepository, times(1)).deleteByUserCarIdIn(carIds);
        verify(expenseRepository, times(1)).deleteByUserCarIdIn(carIds);
        verify(userCarRepository, times(1)).deleteByUserId(userId);
        verify(confirmationTokenRepository, times(1)).deleteByEmail("delete_me@test.com");
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void updateProfile_WhenOldPasswordIsWrong_ShouldReturnErrorMessage() {
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "password", "correct_old_password");

        UpdateProfileRequest request = new UpdateProfileRequest();
        ReflectionTestUtils.setField(request, "oldPassword", "WRONG_password");
        ReflectionTestUtils.setField(request, "newPassword", "new_secure_password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = userService.updateProfile(userId, request);

        assertEquals("Old password is incorrect", result);
        assertEquals("correct_old_password", user.getPassword(), "Пароль не должен был измениться");
    }

    @Test
    void updateProfile_WhenDataIsValid_ShouldUpdateAndReturnNull() {
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "fullName", "Old Name");
        ReflectionTestUtils.setField(user, "password", "correct_old_password");

        UpdateProfileRequest request = new UpdateProfileRequest();
        ReflectionTestUtils.setField(request, "fullName", "New Name");
        ReflectionTestUtils.setField(request, "oldPassword", "correct_old_password");
        ReflectionTestUtils.setField(request, "newPassword", "new_secure_password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = userService.updateProfile(userId, request);

        assertNull(result, "After succeed input method should return null");
        assertEquals("New Name", user.getFullName());
        assertEquals("new_secure_password", user.getPassword());
    }

    @Test
    void registerUser_WithInvalidCyrillicEmail_ShouldStillTryToSaveOrThrowIfValidationFails() {
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "email", "иван@почта.com");
        ReflectionTestUtils.setField(request, "password", "123456");
        ReflectionTestUtils.setField(request, "fullName", "Иван Иванов");
        ReflectionTestUtils.setField(request, "country", "Lithuania");

        Country country = new Country();
        Role role = new Role();
        Currency currency = new Currency();

        when(countryRepository.findByCountryName("Lithuania")).thenReturn(Optional.of(country));
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(role));
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        userService.registerUser(request);

        verify(userRepository, times(1)).save(userCaptor.capture());
        assertEquals("иван@почта.com", userCaptor.getValue().getEmail());
    }

    @Test
    void updateProfile_WhenNewPasswordIsEmpty_ShouldReturnErrorMessage() {
        Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "password", "old_secret");

        UpdateProfileRequest request = new UpdateProfileRequest();
        ReflectionTestUtils.setField(request, "oldPassword", "old_secret");
        ReflectionTestUtils.setField(request, "newPassword", "");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String result = userService.updateProfile(userId, request);

        assertEquals("Enter old and new password", result);
        assertEquals("old_secret", user.getPassword(), "Password in DB should NOT be changed");
    }

    @Test
    void registerUser_WhenCountryNotFound_ShouldThrowException() {
        RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "country", "Narnia");
        ReflectionTestUtils.setField(request, "email", "test@test.com");

        when(countryRepository.findByCountryName("Narnia")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Country not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}