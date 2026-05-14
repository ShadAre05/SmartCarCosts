package lt.teamProject.smartCarCosts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @PostMapping("/reset-password")
    public String saveNewPassword(@RequestParam("password") String newPassword) {
        // В будущем здесь будет сохранение в базу (с JWT и шифрованием)
        // userService.updatePassword(token, newPassword);

        // После успешного сброса отправляем на логин с пометкой
        return "redirect:/login?resetSuccess=true";
    }

    @PostMapping("/forget-password")
    public String processForgetPassword(@RequestParam("email") String email) {
        // TODO: В будущем здесь будет генерация токена и вызов EmailService
        System.out.println("Сброс пароля запрошен для: " + email);

        return "redirect:/forget-password?success";
    }

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