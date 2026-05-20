package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    void deleteByUserCarIdIn(List<Long> userCarIds);

    @Query("""
           SELECT COALESCE(SUM(e.amount), 0)
           FROM Expense e
           WHERE e.userCarId IN :userCarIds
           """)
    BigDecimal getAllTimeTotalByUserCars(List<Long> userCarIds);

    @Query("""
           SELECT COALESCE(SUM(e.amount), 0)
           FROM Expense e
           WHERE e.userCarId IN :userCarIds
           AND e.expenseDate BETWEEN :startDate AND :endDate
           """)
    BigDecimal getTotalByPeriodAndUserCars(
            List<Long> userCarIds,
            LocalDate startDate,
            LocalDate endDate
    );

    List<Expense> findByUserCarIdInOrderByExpenseDateDesc(List<Long> userCarIds);
}