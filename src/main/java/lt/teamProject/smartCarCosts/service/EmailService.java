package lt.teamProject.SmartCarCosts.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Inject mail sender (configured via SMTP)
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sends account confirmation email with verification link
    public void sendConfirmationEmail(String toEmail, String confirmationLink){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Confirm your SmartCarCosts account");
        message.setText(
                "Hello!\n\n" +
                        "Please confirm your account by clicking the link below:\n" +
                        confirmationLink + "\n\n" +
                        "If you did not create this account, ignore this email."
        );

        mailSender.send(message);
    }
}