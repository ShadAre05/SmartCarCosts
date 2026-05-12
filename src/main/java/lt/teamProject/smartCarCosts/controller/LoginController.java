package lt.teamProject.smartCarCosts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/main-interface";
    }

    @GetMapping("/forget-password")
    public String showForgetPasswordPage() {
        return "forget-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "reset-password";
    }
}