package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lt.teamProject.smartCarCosts.entity.ConfirmationToken;
import lt.teamProject.smartCarCosts.repository.ConfirmationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    // Save token
    @Transactional
    public void saveToken(String token, String email) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                email,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5)
        );
        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getTokenData(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Confirmation token not found"));
    }

    // Checks whether the token exists and is not expired.
    public boolean isValidToken(String token){
        Optional<ConfirmationToken> optionalToken = confirmationTokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return false;
        }

        ConfirmationToken confirmationToken = optionalToken.get();

        if (LocalDateTime.now().isAfter(confirmationToken.getExpiresAt())) {
            removeToken(token);
            return false;
        }
        return true;
    }

    public String getEmailByToken(String token) {
        return confirmationTokenRepository.findByToken(token)
                .map(ConfirmationToken::getEmail)
                .orElse(null);
    }

    public boolean existsByEmail(String email) {
        return confirmationTokenRepository.existsByEmail(email);
    }

    public ConfirmationToken getTokenDataByEmail(String email) {
        return confirmationTokenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Confirmation token not found"));
    }

    @Transactional
    public void saveRegistrationToken(String token,
                                      String email,
                                      String fullName,
                                      String password,
                                      Long countryId,
                                      String role) {

        ConfirmationToken confirmationToken = new ConfirmationToken();

        confirmationToken.setToken(token);
        confirmationToken.setEmail(email);
        confirmationToken.setFullName(fullName);
        confirmationToken.setPasswordHash(password);
        confirmationToken.setCountryId(countryId);
        confirmationToken.setRole(role);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    public void removeToken(String token) {
        confirmationTokenRepository.findByToken(token)
                .ifPresent(confirmationTokenRepository::delete);
    }

    @Transactional
    public void removeTokensByEmail(String email) {
        confirmationTokenRepository.deleteByEmail(email);
    }
}

