package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.repository.CurrencyRepository;
import lt.teamProject.smartCarCosts.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lt.teamProject.smartCarCosts.entity.Currency;

@Controller
public class SettingsController {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public SettingsController(UserRepository userRepository, CurrencyRepository currencyRepository) {
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/settings/update")
    public String updateSettings(
            @RequestParam("full_name") String fullName,
            @RequestParam("email") String email,
            @RequestParam(value = "currencyId", required = false) Long currencyId,
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        User user = userRepository.findById(userId).orElseThrow();

        user.setFullName(fullName);
        user.setEmail(email);

        if (currencyId != null) {
            currencyRepository.findById(currencyId).ifPresent(user::setCurrency);
        }

        if (oldPassword != null && !oldPassword.isBlank()
                && newPassword != null && !newPassword.isBlank()) {
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
            }
        }

        userRepository.save(user);

        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userEmail", user.getEmail());

        return "redirect:/main-interface";
    }

    @GetMapping("/settings/delete-account")
    public String deleteAccount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            userRepository.deleteById(userId);
            session.invalidate();
        }

        return "redirect:/login";
    }
}