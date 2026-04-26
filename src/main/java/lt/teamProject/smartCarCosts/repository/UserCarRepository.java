package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.UserCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCarRepository extends JpaRepository<UserCar, Long> {
    List<UserCar> findByUserId(Long userId);

    long countByUserId(Long userId);
}
