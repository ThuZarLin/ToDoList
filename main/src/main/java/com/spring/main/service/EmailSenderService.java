package com.spring.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String email) {
        try {
            // SimpleMailMessage message = new SimpleMailMessage();
            // // message.setFrom("thuzarlintzlmk@gmail.com");
            // message.setTo(toEmail);
            // message.setText("email");
            // message.setSubject("Confirm your email");

            // mailSender.send(message);
            // System.out.println("Mail Sent Successfully");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            //helper.setFrom("your-email@example.com", "Your Name");
            helper.setTo(toEmail);

            helper.setSubject("subject");
            helper.setText(email, true);

            mailSender.send(message);
        } catch (Exception exception) {
            LOGGER.error("Failed to send email: " + exception.getMessage(), exception);
            throw new IllegalStateException("Failed to send email", exception);
        }
    }
}
