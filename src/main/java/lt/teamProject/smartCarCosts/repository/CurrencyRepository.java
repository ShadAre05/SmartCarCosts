package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCurrencySymbol(String symbol);
}