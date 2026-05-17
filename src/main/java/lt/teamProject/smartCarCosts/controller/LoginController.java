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

    // --- 1. ЛОГИН ---
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email,
                               @RequestParam("password") String password,
                               HttpSession session) {
        // Ищем пользователя в базе
        Optional<User> userOpt = userRepository.findByEmail(email);

        // Проверяем, существует ли он и совпадает ли пароль
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            // Если совпадает - сохраняем его ID в сессию (он теперь залогинен)
            session.setAttribute("userId", userOpt.get().getId());
            return "redirect:/main-interface";
        }
        // Если ошибка - возвращаем на логин с пометкой
        return "redirect:/login?error=true";
    }

    // --- 2. ЗАПРОС НА СБРОС ПАРОЛЯ ---
    @GetMapping("/forget-password")
    public String showForgetPasswordPage() {
        return "forget-password";
    }

    @PostMapping("/forget-password")
    public String processForgetPassword(@RequestParam("email") String email) {
        // Проверяем, есть ли такая почта в БД
        if (userRepository.existsByEmail(email)) {

            // 1. Генерируем уникальный токен
            String tokenValue = java.util.UUID.randomUUID().toString();

            // 2. Сохраняем токен в базу на 15 минут
            ConfirmationToken token = new ConfirmationToken(
                    tokenValue, email, java.time.LocalDateTime.now(), java.time.LocalDateTime.now().plusMinutes(15)
            );
            tokenRepository.save(token);

            // 3. ВОТ ГЛАВНАЯ СТРОЧКА: Приклеиваем токен к ссылке!
            String resetLink = "http://localhost:8080/reset-password?token=" + tokenValue;

            // 4. Отправляем письмо с правильной ссылкой
            emailService.sendPasswordResetEmail(email, resetLink);
        }

        return "redirect:/forget-password?success&email=" + email;
    }

    // --- 3. СТРАНИЦА ВВОДА НОВОГО ПАРОЛЯ ---
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token == null || token.isEmpty()) {
            return "redirect:/login"; // Если зашли без токена
        }

        // Проверяем, существует ли токен в базе
        Optional<ConfirmationToken> tokenOpt = tokenRepository.findByToken(token);

        // Если токена нет или его время вышло (прошло > 15 минут)
        if (tokenOpt.isEmpty() || tokenOpt.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            return "redirect:/login?error=invalid_token";
        }

        // Передаем токен в HTML
        model.addAttribute("token", token);
        return "reset-password";
    }

    // --- 4. СОХРАНЕНИЕ НОВОГО ПАРОЛЯ ---
    @PostMapping("/reset-password")
    public String saveNewPassword(@RequestParam("token") String token,
                                  @RequestParam("password") String newPassword) {

        Optional<ConfirmationToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isPresent() && tokenOpt.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            String email = tokenOpt.get().getEmail();

            // Находим юзера и меняем пароль
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setPassword(newPassword); // Сохраняем новый пароль
            userRepository.save(user);

            // Удаляем токен, чтобы его нельзя было использовать дважды
            tokenRepository.delete(tokenOpt.get());

            return "redirect:/login?resetSuccess=true";
        }

        return "redirect:/login?error=invalid_token";
    }

    @GetMapping("/main-interface")
    public String showMainInterface(HttpSession session, Model model) {

        // 1. ПРОВЕРКА БЕЗОПАСНОСТИ: Если в сессии нет userId, значит человек не залогинен!
        if (session.getAttribute("userId") == null) {
            // Выкидываем его на страницу логина
            return "redirect:/login";
        }

        // 2. Если залогинен - достаем его ID из сессии
        Long userId = (Long) session.getAttribute("userId");

        // ... здесь ты можешь достать пользователя из БД по ID и передать его имя в HTML ...
        // User user = userRepository.findById(userId).orElseThrow();
        // model.addAttribute("userName", user.getFullName());

        return "main-interface";
    }
}