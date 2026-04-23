package lt.teamProject.SmartCarCosts.service;

import org.springframework.mail.SimpleMailMessage;
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
}