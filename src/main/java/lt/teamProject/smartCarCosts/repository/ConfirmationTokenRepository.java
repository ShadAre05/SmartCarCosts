package lt.teamProject.smartCarCosts.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import lt.teamProject.smartCarCosts.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

}
