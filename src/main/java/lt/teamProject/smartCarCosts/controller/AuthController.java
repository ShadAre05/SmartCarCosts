package lt.teamProject.smartCarCosts.controller;

import jakarta.validation.Valid;
import lt.teamProject.smartCarCosts.dto.ExpenseDto;
import lt.teamProject.smartCarCosts.dto.RegisterRequest;
import lt.teamProject.smartCarCosts.dto.ReminderRequest;
import lt.teamProject.smartCarCosts.entity.Car;
import lt.teamProject.smartCarCosts.entity.ConfirmationToken;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.repository.CurrencyRepository;
import lt.teamProject.smartCarCosts.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.repository.CountryRepository;
import lt.teamProject.smartCarCosts.repository.ReminderTypeRepository;
import java.util.HashMap;
import java.util.Map;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lt.teamProject.smartCarCosts.dto.CarDto;

@Controller
public class AuthController {

    @Value("${app.base-url}")
    private String baseUrl;

    private final CountryRepository countryRepository;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final ReminderTypeRepository reminderTypeRepository;
    private final ExpenseService expenseService;
    private final CarService carService;
    private final CurrencyRepository currencyRepository;
    private final ReminderService reminderService;

    public AuthController(EmailService emailService,
                          ConfirmationTokenService confirmationTokenService,
                          CountryRepository countryRepository,
                          UserService userService,
                          ReminderTypeRepository reminderTypeRepository,
                          ExpenseService expenseService,
                          CarService carService,
                          CurrencyRepository currencyRepository,
                          ReminderService reminderService) {
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
        this.countryRepository = countryRepository;
        this.userService = userService;
        this.reminderTypeRepository = reminderTypeRepository;
        this.expenseService = expenseService;
        this.carService = carService;
        this.currencyRepository = currencyRepository;
        this.reminderService = reminderService;
    }
    // Show registration page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }

        model.addAttribute("countries", countryRepository.findAll());

        return "register";
    }

    // Handles registration form submission
    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("countries", countryRepository.findAll());
            return "register";
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            model.addAttribute("countries", countryRepository.findAll());
            model.addAttribute("emailExistsError", "User with this email already exists");
            return "register";
        }

        if (confirmationTokenService.existsByEmail(registerRequest.getEmail())) {
            model.addAttribute("countries", countryRepository.findAll());
            model.addAttribute("emailExistsError", "Confirmation email has already been sent");
            return "register";
        }

        Long countryId = countryRepository.findByCountryName(registerRequest.getCountry())
                .orElseThrow(() -> new RuntimeException("Country not found"))
                .getId();

        String token = UUID.randomUUID().toString();
        String link = baseUrl + "/confirm-email?token=" + token;

        confirmationTokenService.saveRegistrationToken(token, registerRequest, countryId);

        session.setAttribute("userEmail", registerRequest.getEmail());
        session.setAttribute("resendAvailableAt", System.currentTimeMillis() + 60_000);

        emailService.sendConfirmationEmail(registerRequest.getEmail(), link);

        return "redirect:/confirm-email-notice";
    }

    // Show confirmation info page with resend timer
    @GetMapping("/confirm-email-notice")
    public String showConfirmEmailNoticePage(Model model, HttpSession session) {
        Long resendAvailableAt = (Long) session.getAttribute("resendAvailableAt");
        String userEmail = (String) session.getAttribute("userEmail");

        long remainingSeconds = 0;

        if (resendAvailableAt != null) {
            long diff = resendAvailableAt - System.currentTimeMillis();
            if (diff > 0) {
                remainingSeconds = (long) Math.ceil(diff / 1000.0);
            }
        }
        model.addAttribute("remainingSeconds", remainingSeconds);
        model.addAttribute("userEmail", userEmail);
        return "confirm-email-notice";
    }


    // Handle reminder form submission
    @PostMapping("/reminders")
    public String createReminder(
            @Valid @ModelAttribute("reminderRequest") ReminderRequest reminderRequest,
            BindingResult bindingResult,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        boolean noReminderOptionSelected =
                !reminderRequest.isMonthBefore()
                        && !reminderRequest.isWeekBefore()
                        && !reminderRequest.isDayBefore();

        if (bindingResult.hasErrors() || noReminderOptionSelected) {
            session.setAttribute("openReminderModal", true);

            if (reminderRequest.getCarId() == null) {
                session.setAttribute("carError", "Please select a car");
            }

            if (reminderRequest.getReminderTypeId() == null) {
                session.setAttribute("reminderTypeError", "Please select a reminder type");
            }

            if (reminderRequest.getReminderDate() == null) {
                session.setAttribute("reminderDateError", "Please select an end date");
            }

            if (noReminderOptionSelected) {
                session.setAttribute("reminderOptionError", "Please select at least one reminder option");
            }

            return "redirect:/main-interface";
        }

        reminderService.createReminder(userId, reminderRequest);

        session.setAttribute("openReminderModal", true);
        session.setAttribute("successMessage", "Notification created successfully");

        return "redirect:/main-interface";
    }

    // Handle email confirmation via token
    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam String token, HttpSession session) {

        if (!confirmationTokenService.isValidToken(token)) {
            return "redirect:/register?error=invalid_token";
        }

        ConfirmationToken tokenData = confirmationTokenService.getTokenData(token);

        userService.registerUserAfterConfirmation(tokenData);

        confirmationTokenService.removeToken(token);

        return "redirect:/login";
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
        String link = baseUrl + "/confirm-email?token=" + token;

        confirmationTokenService.saveToken(token, email);
        emailService.sendConfirmationEmail(email, link);

        // Restart cooldown
        session.setAttribute("resendAvailableAt", System.currentTimeMillis() + 60_000);

        return "redirect:/confirm-email-notice";
    }

    @PostMapping("/reminders/delete")
    public String deleteReminder(@RequestParam List<Long> reminderIds,
                                 HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        reminderService.deleteReminderGroup(userId, reminderIds);

        session.removeAttribute("openReminderModal");
        session.setAttribute("openMyRemindersModal", true);

        return "redirect:/main-interface";
    }


    // Main interface page
    @GetMapping("/main-interface")
    public String mainPage(Model model,
                           HttpSession session,
                           @RequestParam(required = false) LocalDate startDate,
                           @RequestParam(required = false) LocalDate endDate,
                           @RequestParam(required = false) Long carId
    ) {
        // 1. Retrieve the actual user ID from the session
        Long userId = (Long) session.getAttribute("userId");


        // If the user ID is missing (user is not logged in),
        // redirect them to the login page
        if (userId == null) {
            return "redirect:/login";
        }

        // 2. Retrieve the actual user name from the session
        String userName = (String) session.getAttribute("userName");
        if (userName == null){
            userName = "User";
        }

        String userEmail = (String) session.getAttribute("userEmail");
        model.addAttribute("userEmail", userEmail != null ? userEmail : "no-email@example.com");

        // 3. Load cars that belong ONLY to the current user
        User currentUser = userService.getUserById(userId);

        String currencySymbol = currentUser.getCurrency() != null
                ? currentUser.getCurrency().getCurrencySymbol()
                : "€";

        model.addAttribute("currencySymbol", currencySymbol);

        model.addAttribute("userName", currentUser.getFullName());
        model.addAttribute("profileUser", currentUser);

        List<CarDto> cars = carService.getUserCarDtos(userId);
        model.addAttribute("cars", cars);



        model.addAttribute("reminderRequest", new ReminderRequest());
        model.addAttribute("openReminderModal", session.getAttribute("openReminderModal"));
        model.addAttribute("reminderTypes", reminderTypeRepository.findAll());
        model.addAttribute("currencies", currencyRepository.findAll());

        BigDecimal allTimeTotal = carId != null
                ? expenseService.getAllTimeTotalByCar(carId, userId)
                : expenseService.getAllTimeTotal(userId);

        BigDecimal periodTotal = carId != null
                ? expenseService.getPeriodTotalByCar(carId, userId, startDate, endDate)
                : expenseService.getTotalByPeriod(userId, startDate, endDate);
        String selectedPeriod = expenseService.formatSelectedPeriod(startDate, endDate);

        model.addAttribute("allTimeTotal", allTimeTotal);
        model.addAttribute("periodTotal", periodTotal);
        model.addAttribute("selectedPeriod", selectedPeriod);
        model.addAttribute("expenseCategories", expenseService.getExpenseCategories());
        model.addAttribute("expenses", expenseService.getUserExpenses(userId));
        model.addAttribute("selectedCarId", carId);
        if (carId != null) {
            List<ExpenseDto> carExpenses = expenseService.getExpensesByCarIdAndPeriod(carId, userId, startDate, endDate);
            model.addAttribute("carExpenses", carExpenses);

            Map<Long, BigDecimal> categoryTotals = new HashMap<>();
            for (ExpenseDto exp : carExpenses) {
                categoryTotals.merge(exp.getCategoryId(), exp.getAmount(), BigDecimal::add);
            }
            model.addAttribute("categoryTotals", categoryTotals);
        } else {
            model.addAttribute("carExpenses", List.of());
            model.addAttribute("categoryTotals", new HashMap<>());
        }
        model.addAttribute("profileError", session.getAttribute("profileError"));
        model.addAttribute("openEditProfileModal", session.getAttribute("openEditProfileModal"));

        session.removeAttribute("profileError");
        session.removeAttribute("openEditProfileModal");
        model.addAttribute("successMessage", session.getAttribute("successMessage"));
        model.addAttribute("reminderOptionError", session.getAttribute("reminderOptionError"));

        session.removeAttribute("successMessage");
        model.addAttribute("carError", session.getAttribute("carError"));
        model.addAttribute("reminderTypeError", session.getAttribute("reminderTypeError"));
        model.addAttribute("reminderDateError", session.getAttribute("reminderDateError"));
        model.addAttribute("reminderOptionError", session.getAttribute("reminderOptionError"));
        model.addAttribute("currentReminders", reminderService.getUserReminderOverview(userId));

        session.removeAttribute("carError");
        session.removeAttribute("reminderTypeError");
        session.removeAttribute("reminderDateError");
        session.removeAttribute("reminderOptionError");
        session.removeAttribute("openMyRemindersModal");


        return "main-interface";
    }
}
