package lt.teamProject.smartCarCosts.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;

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
                        <a href="%s" style="color:#2d8cff; font-weight:bold; text-decoration:none;">
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

    public void sendReminderCreatedEmail(String to, String reminderType, LocalDate endDate) {
        String subject = "SmartCarCosts reminder created";

        String body = """
                <h2>Reminder created successfully</h2>
                <p>Your reminder has been created.</p>
                <p><b>Type:</b> %s</p>
                <p><b>End date:</b> %s</p>
                <p>We will notify you according to your selected options.</p>
                """.formatted(reminderType, endDate);

        sendHtmlEmail(to, subject, body);
    }

    public void sendReminderNotificationEmail(String to, String reminderType, Integer daysBefore, LocalDate remindAt) {
        String subject = "SmartCarCosts reminder";

        String body = """
                <h2>Reminder from SmartCarCosts</h2>
                <p>This is your reminder.</p>
                <p><b>Type:</b> %s</p>
                <p><b>Notify before:</b> %s days</p>
                <p><b>Reminder date:</b> %s</p>
                """.formatted(reminderType, daysBefore, remindAt);

        sendHtmlEmail(to, subject, body);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}