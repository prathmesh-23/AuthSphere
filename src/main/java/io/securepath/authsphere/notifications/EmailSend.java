package io.securepath.authsphere.notifications;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSend {

    private final JavaMailSender mailSender;

    public EmailSend(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);            // recipient email (user input)
        message.setSubject(subject);  // e.g. "Password Reset OTP"
        message.setText(body);        // e.g. "Your OTP is 123456"
        mailSender.send(message);
    }

}
