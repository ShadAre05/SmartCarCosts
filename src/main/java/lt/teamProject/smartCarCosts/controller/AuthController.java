package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import lt.teamProject.smartCarCosts.dto.ReminderRequest;
import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.repository.CountryRepository;
import lt.teamProject.smartCarCosts.repository.ReminderTypeRepository;
import lt.teamProject.smartCarCosts.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthController {

    private final CountryRepository countryRepository;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final ReminderTypeRepository reminderTypeRepository;
    private final ExpenseService expenseService;
    private final CarService carService;

    public AuthController(EmailService emailService,
                          ConfirmationTokenService confirmationTokenService,
                          CountryRepository countryRepository,
                          UserService userService,
                          ReminderTypeRepository reminderTypeRepository,
                          ExpenseService expenseService,
                          CarService carService) {
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.countryRepository = countryRepository;
        this.userService = userService;
        this.reminderTypeRepository = reminderTypeRepository;
        this.expenseService = expenseService;
        this.carService = carService;
    }

    @GetMapping("/login.html")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/main-interface";
    }

    @GetMapping("/main-interface")
    public String mainPage(Model model,
                           HttpSession session,
                           @RequestParam(required = false) LocalDate startDate,
                           @RequestParam(required = false) LocalDate endDate) {
        String userName = (String) session.getAttribute("userName");
        if (userName == null) userName = "User";
        model.addAttribute("userName", userName);

        Long userId = 1L;
        List<Car> cars = carService.getUserCars(userId);
        model.addAttribute("cars", cars);
        model.addAttribute("reminderRequest", new ReminderRequest());
        model.addAttribute("openReminderModal", false);
        model.addAttribute("reminderTypes", reminderTypeRepository.findAll());

        BigDecimal allTimeTotal = expenseService.getAllTimeTotal();
        BigDecimal periodTotal = expenseService.getTotalByPeriod(startDate, endDate);
        String selectedPeriod = expenseService.formatSelectedPeriod(startDate, endDate);

        model.addAttribute("allTimeTotal", allTimeTotal);
        model.addAttribute("periodTotal", periodTotal);
        model.addAttribute("selectedPeriod", selectedPeriod);
        model.addAttribute("expenseCategories", expenseService.getExpenseCategories());

        return "main-interface";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        model.addAttribute("countries", countryRepository.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                                 BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", countryRepository.findAll());
            return "register";
        }
        userService.registerUser(registerRequest);
        session.setAttribute("userName", registerRequest.getFullName());
        session.setAttribute("userEmail", registerRequest.getEmail());
        String token = UUID.randomUUID().toString();
        confirmationTokenService.saveToken(token, registerRequest.getEmail());
        emailService.sendConfirmationEmail(registerRequest.getEmail(), "http://localhost:8080/confirm-email?token=" + token);
        session.setAttribute("resendAvailableAt", System.currentTimeMillis() + 60_000);
        return "redirect:/confirm-email-notice";
    }

    @GetMapping("/confirm-email-notice")
    public String showConfirmEmailNoticePage(Model model, HttpSession session) {
        Long resendAvailableAt = (Long) session.getAttribute("resendAvailableAt");
        long remainingSeconds = 0;
        if (resendAvailableAt != null) {
            long diff = resendAvailableAt - System.currentTimeMillis();
            if (diff > 0) remainingSeconds = (long) Math.ceil(diff / 1000.0);
        }
        model.addAttribute("remainingSeconds", remainingSeconds);
        return "confirm-email-notice";
    }

    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam String token) {
        if (!confirmationTokenService.isValidToken(token)) return "redirect:/register";
        String email = confirmationTokenService.getEmailByToken(token);
        userService.enableUser(email);
        confirmationTokenService.removeToken(token);
        return "redirect:/main-interface";
    }

    @PostMapping("/add-car")
    public String addCar(@ModelAttribute Car car) {
        carService.addCar(car, 1L);
        return "redirect:/main-interface";
    }
}
