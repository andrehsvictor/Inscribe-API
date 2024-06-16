package andrehsvictor.inscribe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.exception.InscribeException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private MailSender mailSender;

    public void send(String to, String subject, String text) {
        try {
            MimeMessage message = ((JavaMailSender) mailSender).createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            ((JavaMailSender) mailSender).send(message);
        } catch (MessagingException e) {
            throw new InscribeException("Failed to send email", 500);
        }
    }
}
