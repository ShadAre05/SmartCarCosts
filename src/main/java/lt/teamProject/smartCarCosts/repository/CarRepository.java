package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
