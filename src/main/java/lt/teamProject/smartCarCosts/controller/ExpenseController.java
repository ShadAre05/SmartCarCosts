package lt.teamProject.smartCarCosts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExpenseController {

    @GetMapping("/add-cost.html")
    public String showAddCostPage() {
        return "add-cost";
    }

    @PostMapping("/add-cost")
    public String saveCost() {
        System.out.println("Расходы успешно добавлены! (пока понарошку)");

        return "redirect:/main-interface.html";
    }
}