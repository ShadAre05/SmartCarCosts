package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.repository.CurrencyRepository;
import lt.teamProject.smartCarCosts.repository.UserRepository;
import lt.teamProject.smartCarCosts.dto.UpdateProfileRequest;
import lt.teamProject.smartCarCosts.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lt.teamProject.smartCarCosts.entity.Currency;

@Controller
public class SettingsController {

    private final UserService userService;

    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/settings/update-currency")
    public String updateCurrency(@RequestParam Long currencyId,
                                 HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        userService.updateCurrency(userId, currencyId);

        return "redirect:/main-interface";
    }

    @PostMapping("/settings/update-profile")
    public String updateProfile(@ModelAttribute UpdateProfileRequest request,
                                HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        String error = userService.updateProfile(userId, request);

        if (error != null) {
            session.setAttribute("profileError", error);
            session.setAttribute("openEditProfileModal", true);
            return "redirect:/main-interface";
        }

        session.setAttribute("userName", request.getFullName());
        session.setAttribute("userEmail", request.getEmail());

        return "redirect:/main-interface";
    }

    @GetMapping("/settings/delete-account")
    public String deleteAccount(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            userService.deleteUser(userId);
        }

        session.invalidate();

        return "redirect:/main-page";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}