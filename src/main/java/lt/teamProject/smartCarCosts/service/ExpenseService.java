package lt.teamProject.smartCarCosts.service;

import lt.teamProject.smartCarCosts.dto.ExpenseDto;
import lt.teamProject.smartCarCosts.entity.Expense;
import lt.teamProject.smartCarCosts.entity.ExpenseCategory;
import lt.teamProject.smartCarCosts.repository.ExpenseCategoryRepository;
import lt.teamProject.smartCarCosts.repository.ExpenseRepository;
import lt.teamProject.smartCarCosts.repository.UserCarRepository;
import org.springframework.stereotype.Service;
import lt.teamProject.smartCarCosts.entity.UserCar;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
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
                .filter(category -> category != null && category.getName() != null)
                .toList();
    }

    public List<ExpenseDto> getUserExpenses(Long userId) {
        List<Long> userCarIds = userCarRepository.findByUserId(userId).stream()
                .map(UserCar::getId)
                .collect(Collectors.toList());

        if (userCarIds.isEmpty()) return List.of();

        return expenseRepository.findByUserCarIdIn(userCarIds).stream()
                .map(expense -> {
                    String categoryName = expenseCategoryRepository.findById(expense.getCategoryId())
                            .map(ExpenseCategory::getName)
                            .orElse("Unknown");
                    return new ExpenseDto(expense.getId(), expense.getCategoryId(), categoryName,
                            expense.getAmount(), expense.getDescription(), expense.getExpenseDate());
                })
                .collect(Collectors.toList());
    }

    public void addExpense(Long userId, Long carId, Long categoryId, BigDecimal amount, String description) {
        UserCar userCar = userCarRepository.findByUserId(userId).stream()
                .filter(link -> link.getCarId().equals(carId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Car link not found"));

        Expense expense = new Expense();
        expense.setUserCarId(userCar.getId());
        expense.setCategoryId(categoryId);
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setExpenseDate(LocalDate.now());
        expense.setCreatedAt(LocalDateTime.now());

        expenseRepository.save(expense);
    }

    public void deleteExpense(Long expenseId, Long userId) {
        expenseRepository.findById(expenseId).ifPresent(expense -> {
            userCarRepository.findById(expense.getUserCarId()).ifPresent(userCar -> {
                if (userCar.getUserId().equals(userId)) {
                    expenseRepository.delete(expense);
                }
            });
        });
    }

    public List<ExpenseDto> getExpensesByCarId(Long carId, Long userId) {
        UserCar userCar = userCarRepository.findByUserId(userId).stream()
                .filter(link -> link.getCarId().equals(carId))
                .findFirst()
                .orElse(null);

        if (userCar == null) return List.of();

        return expenseRepository.findByUserCarIdIn(List.of(userCar.getId())).stream()
                .map(expense -> {
                    String categoryName = expenseCategoryRepository.findById(expense.getCategoryId())
                            .map(ExpenseCategory::getName)
                            .orElse("Unknown");
                    return new ExpenseDto(expense.getId(), expense.getCategoryId(), categoryName,
                            expense.getAmount(), expense.getDescription(), expense.getExpenseDate());
                })
                .collect(Collectors.toList());
    }
}