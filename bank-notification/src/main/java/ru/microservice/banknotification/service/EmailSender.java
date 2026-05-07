package ru.microservice.banknotification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.microservice.banknotification.exception.EmailSendException;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

    public void send(String email, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setText(message);
            mailSender.send(mailMessage);

        } catch (MailException e) {
            throw new EmailSendException("Ошибка при отправке письма", e);
        }
    }

}
