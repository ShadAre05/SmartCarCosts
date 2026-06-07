package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.ServiceWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceWorkRepository extends JpaRepository<ServiceWork, Integer> {
    List<ServiceWork> findByVisitId(Integer visitId);
    void deleteByVisitId(Integer visitId);
}