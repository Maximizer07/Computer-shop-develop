package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private  ConfirmationTokenService confirmationTokenService;
    @Autowired
    private  EmailService emailSenderService;
    @SneakyThrows
    void sendConfirmationMail(String userMail, String token) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Mail Confirmation Link!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "Thank you for registering. Please click on the below link to activate your account." + "http://localhost:8080/sign-up/confirm?token="
                        + token);
        emailSenderService.sendmail(mailMessage);
    }
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));
    }
    public void signUpUser(User user) {
        System.out.println("ddddd");
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        final User createdUser = userRepository.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
    }

    void confirmUser(ConfirmationToken confirmationToken) {
        System.out.println("sssss");
        final User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }
    public List<User> readAll() {
        return userRepository.findAll();
    }
    public User loadUserById(long id) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with id {0} cannot be found.", id)));
    }
}
