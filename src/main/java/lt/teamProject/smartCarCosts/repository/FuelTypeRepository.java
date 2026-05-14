package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FuelTypeRepository extends JpaRepository<FuelType, Long> {
    Optional<FuelType> findByFuelTypeName(String fuelTypeName);
}