package io.securepath.authsphere.notifications;

import io.securepath.authsphere.constants.ErrorConstant;
import io.securepath.authsphere.response.ApiResponse;
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


    public void sendEmail(Map<String, String> pEmailDetails, String EmailSubject, String ToEmail) {
        try {
            Context context = new Context();
            context.setVariable("username", pEmailDetails.get("username"));
            context.setVariable("resetLink", pEmailDetails.get("resetLink"));
            context.setVariable("expiryHours", pEmailDetails.get("expiryHours"));
            String pBody = templateEngine.process("forgot_pass", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(ToEmail);
            helper.setSubject(EmailSubject);
            helper.setText(pBody, true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e);

        }
    }


}


