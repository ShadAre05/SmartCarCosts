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

    public BigDecimal getAllTimeTotal(){
        return expenseRepository.getAllTimeTotal();
    }

    // Get total amount for selected date period
    public BigDecimal getTotalByPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null){
            return BigDecimal.ZERO;
        }
        return expenseRepository.getTotalByPeriod(startDate, endDate);
    }

    // Format selected period for UI
    public String formatSelectedPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return "select date";
        }
        return startDate + " - " + endDate;
    }

    public List<ExpenseCategory> getExpenseCategories(){
        return expenseCategoryRepository.findAll();
    }

}
