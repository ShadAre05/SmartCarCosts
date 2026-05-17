package lt.teamProject.smartCarCosts.service;

import jakarta.transaction.Transactional;
import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import lt.teamProject.smartCarCosts.dto.UpdateProfileRequest;
import lt.teamProject.smartCarCosts.entity.Country;
import lt.teamProject.smartCarCosts.entity.Currency;
import lt.teamProject.smartCarCosts.entity.Role;
import lt.teamProject.smartCarCosts.repository.CountryRepository;
import lt.teamProject.smartCarCosts.repository.CurrencyRepository;
import lt.teamProject.smartCarCosts.repository.RoleRepository;
import lt.teamProject.smartCarCosts.repository.UserRepository;
import org.springframework.stereotype.Service;
import lt.teamProject.smartCarCosts.entity.User;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final RoleRepository roleRepository;
    private final CurrencyRepository currencyRepository;

    public UserService(UserRepository userRepository, CountryRepository countryRepository, RoleRepository roleRepository, CurrencyRepository currencyRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.roleRepository = roleRepository;
        this.currencyRepository = currencyRepository;
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
        user.setPassword(registerRequest.getPassword());

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
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public String updateProfile(Long userId, UpdateProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean oldPasswordFilled = request.getOldPassword() != null && !request.getOldPassword().isBlank();
        boolean newPasswordFilled = request.getNewPassword() != null && !request.getNewPassword().isBlank();

        if (oldPasswordFilled || newPasswordFilled) {

            if (!oldPasswordFilled || !newPasswordFilled) {
                return "Enter old and new password";
            }

            if (!user.getPassword().equals(request.getOldPassword())) {
                return "Old password is incorrect";
            }

            user.setPassword(request.getNewPassword());
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        return null;
    }
}



