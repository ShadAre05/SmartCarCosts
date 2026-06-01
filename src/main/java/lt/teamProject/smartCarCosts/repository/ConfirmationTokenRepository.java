package lt.teamProject.smartCarCosts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import lt.teamProject.smartCarCosts.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByToken(String token);

}
