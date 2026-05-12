package lt.teamProject.smartCarCosts.repository;
import lt.teamProject.smartCarCosts.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {}