package andrehsvictor.inscribe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.exception.InscribeException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(String to, String subject, String text) {
        MimeMailMessage message = new MimeMailMessage(javaMailSender.createMimeMessage());
        MimeMessage mimeMessage = message.getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
        } catch (MessagingException e) {
            throw new InscribeException("Failed to send email", 500);
        }
        javaMailSender.send(mimeMessage);
    }
}
