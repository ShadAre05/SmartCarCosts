package lt.teamProject.smartCarCosts.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConfirmationTokenService {

    // In-memory storage: - email mapping
    private final Map<String, String> tokenEmails = new ConcurrentHashMap<>();
    // In-memory storage: token - expiration timestamp
    private final Map<String, Long> tokenExpires = new ConcurrentHashMap<>();

    // Save token with 15-minute expiration
    public void saveToken(String token, String email){
        tokenEmails.put(token, email);
        tokenExpires.put(token, System.currentTimeMillis() + 15 * 60 * 1000);
    }

    // Checks if token exists and is still valid
    public boolean isValidToken(String token){
        Long expiresAt = tokenExpires.get(token);

        if (expiresAt == null){
            return false;
        }

        // Remove token if expired
        if (System.currentTimeMillis() > expiresAt){
            removeToken(token);
            return false;
        }
        return tokenEmails.containsKey(token);
    }

    // Get email associated with token
    public String getEmailByToken(String token){
        return tokenEmails.get(token);
    }

    // Remove token from storage
    public void removeToken(String token){
        tokenEmails.remove(token);
        tokenExpires.remove(token);
    }
}
