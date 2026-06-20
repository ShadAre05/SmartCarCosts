package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.ServiceVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceVisitRepository extends JpaRepository<ServiceVisit, Integer> {
    List<ServiceVisit> findByCarId(Integer carId);
    void deleteByCarId(Integer carId);
}