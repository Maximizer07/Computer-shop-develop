package com.example.demo.User;

import com.example.demo.ConfirmationToken.ConfirmationToken;
import com.example.demo.ConfirmationToken.ConfirmationTokenService;
import com.example.demo.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailService emailSenderService;
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
        System.out.println(userRepository.findAll());
        final Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("com.example.demo.User.User with email {0} cannot be found.", email)));
    }
    public void signUpUser(User user) {
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setUserRole(UserRole.USER);
        final User createdUser = userRepository.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
    }

    public void confirmUser(ConfirmationToken confirmationToken) {
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
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("com.example.demo.User.User with id {0} cannot be found.", id)));
    }
    public void updateUserRole(long id, UserRole role) {
        Optional<User> u = userRepository.findById(id);
         User uu = u.get();
        uu.setUserRole(role);
        userRepository.save(uu);
    }
    public void updateUserName(long id, String name) {
        Optional<User> u = userRepository.findById(id);
        User uu = u.get();
        uu.setName(name);
        userRepository.save(uu);
    }
    public void updateSurName(long id, String surname) {
        Optional<User> u = userRepository.findById(id);
        User uu = u.get();
        uu.setSurname(surname);
        userRepository.save(uu);
    }
    public void deleteUser(long id){
        userRepository.deleteUserById(id);
    }
}
