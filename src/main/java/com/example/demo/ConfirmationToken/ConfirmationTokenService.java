package com.example.demo.ConfirmationToken;

import com.example.demo.CartItem.Cart_Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * сервис работы с токенами
 */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    /**
     * репозиторий работы с токенами
     */
    private final ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * сохранение токена
     * @param confirmationToken токен подтверждения
     */
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {

        confirmationTokenRepository.save(confirmationToken);
    }

    /**
     * получение всех токенов
     * @return список всех токенов
     */
    public List<ConfirmationToken> readAll() {
        return confirmationTokenRepository.findAll();
    }

    /**
     * удаление токена по id
     * @param id id токена
     */
    public void deleteConfirmationToken(Long id) {

        confirmationTokenRepository.deleteById(id);
    }

    /**
     * поиск токена по содержанию
     * @param token содержание токена
     * @return токен ввиде опционала
     */
    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {

        return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
    }
}
