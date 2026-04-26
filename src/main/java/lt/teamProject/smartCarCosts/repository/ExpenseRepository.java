package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseRepository extends JpaRepository<Expense, Long > {

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    BigDecimal getAllTimeTotal();

    @Query("""
           SELECT COALESCE(SUM(e.amount), 0)
           FROM Expense e
           WHERE e.expenseDate BETWEEN :startDate AND :endDate
           """)
    BigDecimal getTotalByPeriod(LocalDate startDate, LocalDate endDate);
}
