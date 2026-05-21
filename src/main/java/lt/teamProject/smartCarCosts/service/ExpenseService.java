package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.entity.ExpenseCategory;
import lt.teamProject.smartCarCosts.repository.ExpenseCategoryRepository;
import lt.teamProject.smartCarCosts.repository.ExpenseRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;
    private final UserCarRepository userCarRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseCategoryRepository expenseCategoryRepository,
                          UserCarRepository userCarRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
        this.userCarRepository = userCarRepository;
    }

    public BigDecimal getAllTimeTotal(Long userId) {
        List<Long> userCarIds = userCarRepository.findByUserId(userId)
                .stream()
                .map(userCar -> userCar.getId())
                .toList();

        if (userCarIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return expenseRepository.getAllTimeTotalByUserCars(userCarIds);
    }

    public BigDecimal getTotalByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return BigDecimal.ZERO;
        }

        List<Long> userCarIds = userCarRepository.findByUserId(userId)
                .stream()
                .map(userCar -> userCar.getId())
                .toList();

        if (userCarIds.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return expenseRepository.getTotalByPeriodAndUserCars(
                userCarIds,
                startDate,
                endDate
        );
    }

    public String formatSelectedPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return "XXXX-XX-XX - XXXX-XX-XX";
        }

        return startDate + " - " + endDate;
    }

    public List<ExpenseCategory> getExpenseCategories() {
        return expenseCategoryRepository.findAll()
                .stream()
                .filter(category -> category != null)
                .filter(category -> category.getName() != null)
                .toList();
    }
}