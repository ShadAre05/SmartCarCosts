package lt.teamProject.smartCarCosts.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Inject mail sender (configured via SMTP)
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sends account confirmation email with verification link
    public void sendConfirmationEmail(String toEmail, String confirmationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = """
                    <p>Hello!</p>
                    <p>Please confirm your account by clicking the link below:</p>
                    <p>
                        <a href="%s" style="color:#2d8cff; font-weight:bold; text-decoration:none;">
                            SmartCarCosts
                        </a>
                    </p>
                    <p>If you did not create this account, ignore this email.</p>
                    """.formatted(confirmationLink);
            helper.setTo(toEmail);
            helper.setSubject("Confirm your SmartCarCosts account");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }

    // Метод для отправки ссылки на сброс пароля
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = """
                    <p>Hello!</p>
                    <p>You requested a password reset for your SmartCarCosts account.</p>
                    <p>Click the link below to set a new password:</p>
                    <p>
                        <a href="%s" style="display:inline-block; padding:10px 20px; background-color:#5ab2da; color:#000; font-weight:bold; text-decoration:none; border-radius:4px;">
                            Reset Password
                        </a>
                    </p>
                    <p>This link is valid for 15 minutes. If you did not request this, please ignore this email.</p>
                    """.formatted(resetLink);
            helper.setTo(toEmail);
            helper.setSubject("Reset your SmartCarCosts password");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}