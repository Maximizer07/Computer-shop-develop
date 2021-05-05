package com.example.demo;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
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
        mailMessage.setSubject("Ссылка для подтверждения аккаунта на сайте Технорай!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "Спасибо, что зарегистрировались на нашем сайте. Пожалуйста, перейдите по ссылке для активации вашего аккаунта." + "http://localhost:8080/sign-up/confirm?token="
                        + token);
        emailSenderService.sendmail(mailMessage);
    }
    public boolean uniqueEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));
    }
    public void signUpUser(User user) {
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        final User createdUser = userRepository.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
    }

    void confirmUser(ConfirmationToken confirmationToken) {
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
