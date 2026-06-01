package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.dto.UpdateProfileRequest;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        User updatedUser = userService.getUserById(userId);

        session.setAttribute(
                "currencySymbol",
                updatedUser.getCurrency().getCurrencySymbol()
        );

        return redirectByRole(updatedUser);
    }

    @PostMapping("/settings/update-profile")
    public String updateProfile(@ModelAttribute UpdateProfileRequest request,
                                HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userService.getUserById(userId);

        String error = userService.updateProfile(userId, request);

        if (error != null) {
            session.setAttribute("profileError", error);
            session.setAttribute("openEditProfileModal", true);
            return redirectByRole(currentUser);
        }

        User updatedUser = userService.getUserById(userId);

        session.setAttribute("userName", updatedUser.getFullName());

        return redirectByRole(updatedUser);
    }

    @PostMapping("/settings/delete-account")
    public String deleteAccount(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            userService.deleteUser(userId);
        }

        session.invalidate();

        return "redirect:/";
    }

    private String redirectByRole(User user) {
        if ("SERVICE".equals(user.getRole().getRole())) {
            return "redirect:/service-main-interface";
        }

        return "redirect:/main-interface";
    }
}