package lt.teamProject.SmartCarCosts.controller;

import jakarta.validation.Valid;
import lt.teamProject.SmartCarCosts.dto.RegisterRequest;
import lt.teamProject.SmartCarCosts.dto.ReminderRequest;
import lt.teamProject.SmartCarCosts.service.ConfirmationTokenService;
import lt.teamProject.SmartCarCosts.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthController {

    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthController(EmailService emailService,
                          ConfirmationTokenService confirmationTokenService) {
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
    }
    // Show registration page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }

        return "register";
    }
    // Handles registration form submission
    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult, HttpSession session
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        // Save user data in session
        session.setAttribute("userName", registerRequest.getFullName());
        session.setAttribute("userEmail", registerRequest.getEmail());
        // Create confirmation token
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:8080/confirm-email?token=" + token;

        confirmationTokenService.saveToken(token, registerRequest.getEmail());
        emailService.sendConfirmationEmail(registerRequest.getEmail(), link);

        // Start 60s resend cooldown
        session.setAttribute("resendAvailableAt", System.currentTimeMillis() + 60_000);

        return "redirect:/confirm-email-notice";
    }


    // Show confirmation info page with resend timer
    @GetMapping("/confirm-email-notice")
    public String showConfirmEmailNoticePage(Model model, HttpSession session) {
        Long resendAvailableAt = (Long) session.getAttribute("resendAvailableAt");

        long remainingSeconds = 0;

        if (resendAvailableAt != null) {
            long diff = resendAvailableAt - System.currentTimeMillis();
            if (diff > 0) {
                remainingSeconds = (long) Math.ceil(diff / 1000.0);
            }
        }
        model.addAttribute("remainingSeconds", remainingSeconds);
        return "confirm-email-notice";
    }


    // Handle reminder form submission
    @PostMapping("/reminders")
    public String createReminder(
            @Valid @ModelAttribute("reminderRequest") ReminderRequest reminderRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ){
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            userName = "User";
        }

        model.addAttribute("userName", userName);

        List<String> cars = new ArrayList<>();
        model.addAttribute("cars", cars);

        // Custom validation: at least one checkbox must be selected
        boolean noReminderOptionSelected =
                !reminderRequest.isMonthBefore()
                        && !reminderRequest.isWeekBefore()
                        && !reminderRequest.isDayBefore();

        if (noReminderOptionSelected) {
            model.addAttribute("reminderOptionError", "Select at least one reminder option");
        }

        if (bindingResult.hasErrors() || noReminderOptionSelected) {
            model.addAttribute("openReminderModal", true);
            return "main-interface";
        }

        // Temporary success message
        model.addAttribute("successMessage", "Notification created successfully");
        model.addAttribute("openReminderModal", true);
        model.addAttribute("reminderRequest", new ReminderRequest());

        return "main-interface";
    }
    @GetMapping("/add-car")
    public String addCarPage() {
        return "add-car";
    }
    @GetMapping("/my-reminders")
    public String myRemindersPage(){
        return"my-reminders";
    }

    // Handle email confirmation via token
    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam String token) {

        // Invalid or expired token - back to register
        if (!confirmationTokenService.isValidToken(token)) {
            return "redirect:/register";
        }

        String email = confirmationTokenService.getEmailByToken(token);
        System.out.println("Confirmed email: " + email);

        // Remove token after success
        confirmationTokenService.removeToken(token);

        return "redirect:/main-interface";
    }

    // Resend confirmation email (with cooldown)
    @PostMapping("/resend-confirmation")
    public String resendConfirmation(HttpSession session){
        String email = (String) session.getAttribute("userEmail");
        Long resendAvailableAt = (Long) session.getAttribute("resendAvailableAt");

        // if session lost - go back to register
        if (email == null || email.isBlank()){
            return "redirect:/register";
        }

        // Prevent resend during cooldown
        if (resendAvailableAt != null && System.currentTimeMillis() < resendAvailableAt){
            return "redirect:/confirm-email-notice";
        }

        // Generate new token
        String token = UUID.randomUUID().toString();
        String link = "http://localhost:8080/confirm-email?token=" + token;

        confirmationTokenService.saveToken(token, email);
        emailService.sendConfirmationEmail(email, link);

        // Restart cooldown
        session.setAttribute("resendAvailableAt", System.currentTimeMillis() + 60_000);

        return "redirect:/confirm-email-notice";
    }

    // Main interface page
    @GetMapping("/main-interface")
    public String mainPage(Model model, HttpSession session) {

        String userName = (String) session.getAttribute("userName");

        // Fallback name if session is empty
        if (userName == null){
            userName = "User";
        }
        model.addAttribute("userName", userName);

        // Temporary empty cars list (until DB is added)
        List<String> cars = new ArrayList<>();
        model.addAttribute("cars", cars);

        model.addAttribute("reminderRequest", new ReminderRequest());
        model.addAttribute("openReminderModal", false);

        return "main-interface";
    }



}
