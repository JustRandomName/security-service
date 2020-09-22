package security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(final String username, final String userId) {
        mailSender.send(prepareEmailForConfirm(username, userId));
    }

    private SimpleMailMessage prepareEmailForConfirm(final String username, final String userId) {
        final SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(username);
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText("To confirm your e-mail address, please click the link below:\n" +
                "http://localhost:8091/confirm?userId=" + userId);
        registrationEmail.setFrom("noreply@domain.com");

        return registrationEmail;
    }
}