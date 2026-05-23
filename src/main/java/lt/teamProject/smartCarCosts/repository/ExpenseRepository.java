package lt.teamProject.smartCarCosts.repository;

import lt.teamProject.smartCarCosts.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT SUM(e.amount) FROM Expense e")
    BigDecimal getAllTimeTotal();

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Expense> findByUserCarIdIn(List<Long> userCarIds);

    @Transactional
    void deleteByUserCarId(Long userCarId);
}
