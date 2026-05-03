package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Model; // Импортируем класс Model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}