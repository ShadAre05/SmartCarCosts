package lt.teamProject.smartCarCosts.controller;

import jakarta.servlet.http.HttpSession;
import lt.teamProject.smartCarCosts.entity.ConfirmationToken;
import lt.teamProject.smartCarCosts.entity.User;
import lt.teamProject.smartCarCosts.repository.ConfirmationTokenRepository;
import lt.teamProject.smartCarCosts.repository.UserRepository;
import lt.teamProject.smartCarCosts.service.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LoginController {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ConfirmationTokenRepository tokenRepository;

    public LoginController(EmailService emailService, UserRepository userRepository, ConfirmationTokenRepository tokenRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    // --- 1. LOGIN ---
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               jakarta.servlet.http.HttpServletResponse response) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();

            String jwtToken = lt.teamProject.smartCarCosts.util.JwtUtil.generateToken(
                    user.getId(), user.getFullName(), user.getEmail()
            );

            jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("jwt", jwtToken);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(86400);

            response.addCookie(jwtCookie);

            return "redirect:/main-interface";
        }
        return "redirect:/login?error=true";
    }

    // --- 2. PASSWORD RESET REQUEST ---
    @GetMapping("/forget-password")
    public String showForgetPasswordPage() {
        return "forget-password";
    }

    @PostMapping("/forget-password")
    public String processForgetPassword(@RequestParam("email") String email) {
        // Check if the email exists in the database
        if (userRepository.existsByEmail(email)) {

            // 1. Generate a unique token
            String tokenValue = java.util.UUID.randomUUID().toString();

            // 2. Save the token for 15 minutes
            ConfirmationToken token = new ConfirmationToken(
                    tokenValue, email, java.time.LocalDateTime.now(), java.time.LocalDateTime.now().plusMinutes(15)
            );
            tokenRepository.save(token);

            // 3. Attach the token to the reset link
            String resetLink = "http://localhost:8080/reset-password?token=" + tokenValue;

            // 4. Send the password reset email
            emailService.sendPasswordResetEmail(email, resetLink);
        }

        return "redirect:/forget-password?success&email=" + email;
    }

    // --- 3. NEW PASSWORD PAGE ---
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.isEmpty()) {
            return "redirect:/login"; // Redirect if the token is missing
        }

        // Check whether the token exists in the database
        Optional<ConfirmationToken> tokenOpt = tokenRepository.findByToken(token);

        // Redirect if the token is invalid or expired
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return "redirect:/login?error=invalid_token";
        }

        // Pass the token to the HTML page
        model.addAttribute("token", token);
        return "reset-password";
    }

    // --- 4. SAVE NEW PASSWORD ---
    @PostMapping("/reset-password")
    public String saveNewPassword(@RequestParam("token") String token,
                                  @RequestParam("password") String newPassword) {

        Optional<ConfirmationToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isPresent() && tokenOpt.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            String email = tokenOpt.get().getEmail();

            // Find the user and update the password
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setPassword(newPassword);
            userRepository.save(user);

            tokenRepository.delete(tokenOpt.get());

            return "redirect:/login?resetSuccess=true";
        }

        return "redirect:/login?error=invalid_token";
    }

    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.http.HttpSession session) {
        session.invalidate();


        jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("jwt", null);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/login";
    }

}