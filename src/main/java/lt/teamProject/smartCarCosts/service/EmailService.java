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
    public void sendConfirmationEmail(String toEmail, String userName, String confirmationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">

                    <h2 style="color:#2d8cff;">
                        Welcome to SmartCarCosts
                    </h2>

                    <p>Hello, <b>%s</b>,</p>

                    <p>
                        Thank you for registering with SmartCarCosts.
                    </p>

                    <p>
                        To activate your account and complete the registration process,
                        please confirm your email address by clicking the button below:
                    </p>

                    <div style="text-align:center; margin:30px 0;">
                        <a href="%s"
                           style="
                                background:#58ACE0;
                                color:white;
                                padding:14px 32px;
                                text-decoration:none;
                                border-radius:6px;
                                font-weight:bold;
                                display:inline-block;">
                            Confirm Email
                        </a>
                    </div>

                    <p>
                        This confirmation link will expire in <b>15 minutes</b>.
                    </p>

                    <p>
                        If you did not create a SmartCarCosts account,
                        you can safely ignore this email.
                    </p>

                    <br>

                    <p>
                        Best regards,<br>
                        <b>SmartCarCosts Team</b>
                    </p>

                </div>
                """.formatted(userName, confirmationLink);

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