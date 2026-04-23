package lt.teamProject.smartCarCosts.service;

import jakarta.transaction.Transactional;
import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import lt.teamProject.smartCarCosts.entity.Country;
import lt.teamProject.smartCarCosts.entity.Role;
import lt.teamProject.smartCarCosts.repository.CountryRepository;
import lt.teamProject.smartCarCosts.repository.RoleRepository;
import lt.teamProject.smartCarCosts.repository.UserRepository;
import org.springframework.stereotype.Service;
import lt.teamProject.smartCarCosts.entity.User;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, CountryRepository countryRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.roleRepository = roleRepository;
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

        // Save user to database
        userRepository.save(user);
    }

    // Enable user account after confirmation
    @Transactional
    public void enableUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
    }
}
