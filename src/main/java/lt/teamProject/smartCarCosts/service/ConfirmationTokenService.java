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

    public ConfirmationToken getTokenData(String token) {
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    @Transactional
    public void saveRegistrationToken(String token, RegisterRequest request, Long countryId) {
        ConfirmationToken confirmationToken = new ConfirmationToken();

        confirmationToken.setToken(token);
        confirmationToken.setEmail(request.getEmail());
        confirmationToken.setFullName(request.getFullName());
        confirmationToken.setPasswordHash(request.getPassword());
        confirmationToken.setCountryId(countryId);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    public void removeToken(String token) {
        confirmationTokenRepository.findByToken(token)
                .ifPresent(confirmationTokenRepository::delete);
    }
}

