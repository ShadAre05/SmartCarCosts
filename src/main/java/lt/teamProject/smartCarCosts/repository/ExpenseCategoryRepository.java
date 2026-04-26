package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
}
