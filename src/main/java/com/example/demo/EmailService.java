package com.example.demo;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * сервис отправки сообщений на почту
 * @author mike
 */
@Service
@Transactional
@AllArgsConstructor
public class EmailService {
    /**
     * javaMailSender
     */
    private JavaMailSender javaMailSender;

    /**
     * @param email почта пользователя
     * @throws AddressException если адрес неправильный
     * @throws MessagingException если сообщение неправильного
     * @throws IOException если не существует почта
     */
    public void sendmail(SimpleMailMessage email) throws AddressException, MessagingException, IOException {
        javaMailSender.send(email);
    }
}
