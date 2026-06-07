package lt.teamProject.smartCarCosts.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendConfirmationEmail_ShouldCallMailSender() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendConfirmationEmail("student@test.com", "USER","http://localhost:8080/confirm");

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendReminderCreatedEmail_ShouldCallMailSender() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendReminderCreatedEmail("driver@test.com", "Insurance", LocalDate.of(2026, 12, 31));

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendPasswordResetEmail_ShouldCallMailSender() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendPasswordResetEmail("user@test.com", "http://reset.link");

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
