package lt.teamProject.smartCarCosts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SettingsController {

    @PostMapping("/settings/update")
    public String updateSettings() {
        return "redirect:/main-interface";
    }

    @GetMapping("/settings/delete-account")
    public String deleteAccount() {
        return "redirect:/login";
    }
}