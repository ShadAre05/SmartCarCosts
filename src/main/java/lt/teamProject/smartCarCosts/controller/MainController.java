package lt.teamProject.smartCarCosts.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // First page of the application
    @GetMapping("/")
    public String showMainPage() {
        return "main-page";
    }

    // Включаем отображение main-interface
    @GetMapping("/main-interface.html")
    public String showMainInterface() {
        return "main-interface";
    }
}
