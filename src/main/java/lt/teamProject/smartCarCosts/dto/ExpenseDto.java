package lt.teamProject.smartCarCosts.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDto {
    private Long id;
    private String categoryName;
    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;

    public ExpenseDto(Long id, String categoryName, BigDecimal amount, String description, LocalDate expenseDate) {
        this.id = id;
        this.categoryName = categoryName;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
    }

    public Long getId() { return id; }
    public String getCategoryName() { return categoryName; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getExpenseDate() { return expenseDate; }
}