package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.entity.ExpenseCategory;
import lt.teamProject.smartCarCosts.repository.ExpenseCategoryRepository;
import lt.teamProject.smartCarCosts.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository expenseCategoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          ExpenseCategoryRepository expenseCategoryRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseCategoryRepository = expenseCategoryRepository;
    }

    public BigDecimal getAllTimeTotal() {
        return expenseRepository.getAllTimeTotal();
    }

    public BigDecimal getTotalByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return BigDecimal.ZERO;
        }

        return expenseRepository.getTotalByPeriod(startDate, endDate);
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
