package lt.teamProject.smartCarCosts.service;

import jakarta.transaction.Transactional;
import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import lt.teamProject.smartCarCosts.dto.UpdateProfileRequest;
import lt.teamProject.smartCarCosts.entity.*;
import lt.teamProject.smartCarCosts.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final RoleRepository roleRepository;
    private final CurrencyRepository currencyRepository;
    private final UserCarRepository userCarRepository;
    private final ReminderRepository reminderRepository;
    private final ExpenseRepository expenseRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CountryRepository countryRepository, RoleRepository roleRepository, CurrencyRepository currencyRepository, UserCarRepository userCarRepository, ReminderRepository reminderRepository, ExpenseRepository expenseRepository, ConfirmationTokenRepository confirmationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.roleRepository = roleRepository;
        this.currencyRepository = currencyRepository;
        this.userCarRepository = userCarRepository;
        this.reminderRepository = reminderRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.expenseRepository = expenseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Check if user already exists by email
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Register a new user
    @Transactional
    public void registerUser(RegisterRequest registerRequest) {

        // Find country by name from DB
        Country country = countryRepository.findByCountryName(registerRequest.getCountry())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        // Assign default USER role
        Role role = roleRepository.findByRole("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));

        // Create new user entity
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());

        // Set raw password (should be hashed in real app)
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        user.setCountry(country);
        user.setRole(role);

        // User is disabled until email confirmation
        user.setEnabled(false);

        // Default currency EUR
        Currency defaultCurrency = currencyRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        user.setCurrency(defaultCurrency);

        // Save user to database
        userRepository.save(user);
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow()
                .getId();
    }

    // Enable user account after confirmation
    @Transactional
    public void enableUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
    }

    @Transactional
    public void deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserCar> userCars = userCarRepository.findByUserId(userId);

        List<Long> userCarIds = userCars.stream()
                .map(UserCar::getId)
                .toList();

        if (!userCarIds.isEmpty()) {
            reminderRepository.deleteByUserCarIdIn(userCarIds);
            expenseRepository.deleteByUserCarIdIn(userCarIds);
        }

        userCarRepository.deleteByUserId(userId);

        confirmationTokenRepository.deleteByEmail(user.getEmail());

        userRepository.deleteById(userId);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void updateCurrency(Long userId, Long currencyId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        user.setCurrency(currency);

        userRepository.save(user);
    }

    @Transactional
    public void registerUserAfterConfirmation(ConfirmationToken tokenData) {
        Country country = countryRepository.findById(tokenData.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        Role role = roleRepository.findByRole(tokenData.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Currency defaultCurrency = currencyRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        User user = new User();
        user.setFullName(tokenData.getFullName());
        user.setEmail(tokenData.getEmail());
        user.setPassword(passwordEncoder.encode(tokenData.getPasswordHash()));
        user.setCountry(country);
        user.setRole(role);
        user.setCurrency(defaultCurrency);
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Transactional
    public String updateProfile(Long userId, UpdateProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (request.getFullName() != null
                && !request.getFullName().isBlank()) {

            user.setFullName(request.getFullName());
        }

        boolean oldPasswordFilled =
                request.getOldPassword() != null
                        && !request.getOldPassword().isBlank();

        boolean newPasswordFilled =
                request.getNewPassword() != null
                        && !request.getNewPassword().isBlank();

        if (oldPasswordFilled || newPasswordFilled) {

            if (!oldPasswordFilled || !newPasswordFilled) {
                return "Enter old and new password";
            }

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return "Old password is incorrect";
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        return null;
    }
}



