package lt.teamProject.smartCarCosts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings.html")
    public String showSettingsPage() {
        return "settings";
    }

    @PostMapping("/settings/update")
    public String updateSettings() {
        System.out.println("Настройки профиля сохранены! (пока понарошку)");

        return "redirect:/main-interface.html";
    }
}