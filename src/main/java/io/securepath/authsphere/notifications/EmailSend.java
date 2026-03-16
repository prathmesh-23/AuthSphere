package io.securepath.authsphere.notifications;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailSend {

    private final JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public EmailSend(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(Map<String, String> pEmailDetails) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", pEmailDetails.get("username"));
        context.setVariable("resetLink", pEmailDetails.get("resetLink"));
        context.setVariable("expiryHours", pEmailDetails.get("expiryHours"));
        String pBody = templateEngine.process("forgot_pass", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(pEmailDetails.get("userEmail"));
        helper.setSubject(pEmailDetails.get("subject"));
        helper.setText(pBody, true); // true = HTML

        mailSender.send(message);
    }
}


