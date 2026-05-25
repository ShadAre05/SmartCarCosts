package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
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
                          @RequestParam("categoryId") Long categoryId, // <-- ТЕПЕРЬ ТУТ ID КАТЕГОРИИ
                          @RequestParam("amount") BigDecimal amount,
                          @RequestParam(value = "description", required = false) String description,
                          HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        expenseService.addExpense(userId, carId, categoryId, amount, description);

        return "redirect:/main-interface?costAdded";
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