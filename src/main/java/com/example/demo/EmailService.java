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
@Service
@Transactional
@AllArgsConstructor
public class EmailService {
    private JavaMailSender javaMailSender;
    public void sendmail(SimpleMailMessage email) throws AddressException, MessagingException, IOException {
        javaMailSender.send(email);
    }
}
