package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import lt.teamProject.smartCarCosts.service.ExpenseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/costs/add")
    public String addCost(@RequestParam("carId") Long carId,
                          @RequestParam("categoryId") Long categoryId,
                          @RequestParam("amount") BigDecimal amount,
                          @RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "expenseDate", required = false) LocalDate expenseDate,
                          HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        expenseService.addExpense(userId, carId, categoryId, amount, description, expenseDate);

        return "redirect:/main-interface?carId=" + carId + "&costAdded";
    }

    @PostMapping("/costs/delete")
    public String deleteCost(@RequestParam("expenseId") Long expenseId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            expenseService.deleteExpense(expenseId, userId);
        }
        return "redirect:/main-interface?costDeleted";
    }
}