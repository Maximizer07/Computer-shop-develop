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

/**
 * сервис работы с пользователями
 * @author mike
 */
@Service
@Transactional
public class UserService implements UserDetailsService {
    /**
     * конструктор
     * @param userRepository репозиторий работы с пользователями
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * репозиторий работы с пользователями
     */
    @Autowired
    private UserRepository userRepository;
    /**
     *  bCryptPasswordEncoder
     */
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    /**
     * токен подтверждения аккаунта
     */
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    /**
     * сервис отправки письма на почту
     */
    @Autowired
    private EmailService emailSenderService;

    /**
     * отправка сообщения на почту с подтверждением аккаунта
     * @param userMail почта пользователя
     * @param token содержание токена
     */
    @SneakyThrows
    void sendConfirmationMail(String userMail, String token) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Ссылка для подтверждения аккаунта на сайте Технорай!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText(
                "Спасибо, что зарегистрировались на нашем сайте. Пожалуйста, перейдите по ссылке для активации вашего аккаунта." + "https://tehnoparadise.herokuapp.com/sign-up/confirm?token="
                        + token);
        emailSenderService.sendmail(mailMessage);
    }

    /**
     * проверка уникальности почты
     * @param email почта пользователя
     * @return результат проверки
     */
    public boolean uniqueEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * поиск пользователей
     * @param email почта пользователя
     * @return пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("com.example.demo.User.User with email {0} cannot be found.", email)));
    }

    /**
     * создание аккаунта
     * @param user пользователь
     */
    public void signUpUser(User user) {
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setUserRole(UserRole.USER);
        final User createdUser = userRepository.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
    }

    /**
     * подтверждения пользователя
     * @param confirmationToken токен подтверждения
     */
    public void confirmUser(ConfirmationToken confirmationToken) {
        final User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    /**
     * получение всех пользователей
     * @return список пользователей
     */
    public List<User> readAll() {
        return userRepository.findAll();
    }

    /**
     * поиск пользователя по id
     * @param id id пользователя
     * @return пользователь
     * @throws UsernameNotFoundException если пользователь не найден
     */
    public User loadUserById(long id) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("com.example.demo.User.User with id {0} cannot be found.", id)));
    }

    /**
     * обновление роли пользователя
     * @param id id пользователя
     * @param role роль пользователя
     */
    public void updateUserRole(long id, UserRole role) {
        Optional<User> u = userRepository.findById(id);
         User uu = u.get();
        uu.setUserRole(role);
        userRepository.save(uu);
    }

    /**
     * обновление имени пользователя
     * @param id пользователя
     * @param name имя пользователя
     */
    public void updateUserName(long id, String name) {
        Optional<User> u = userRepository.findById(id);
        User uu = u.get();
        uu.setName(name);
        userRepository.save(uu);
    }

    /**
     * обновление фамилии пользователя
     * @param id пользователя
     * @param surname фамилия пользователя
     */
    public void updateSurName(long id, String surname) {
        Optional<User> u = userRepository.findById(id);
        User uu = u.get();
        uu.setSurname(surname);
        userRepository.save(uu);
    }

    /**
     * удаление пользователя
     * @param id id пользователя
     */
    public void deleteUser(long id){
        userRepository.deleteUserById(id);
    }
}
