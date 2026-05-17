package lt.teamProject.smartCarCosts.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // First page of the application
    @GetMapping("/main-page")
    public String showMainPage() {
        return "main-page";
    }
}
