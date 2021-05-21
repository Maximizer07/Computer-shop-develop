package com.example.demo.ConfirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * репозиторий работы с токенами
 * @author mike
 */
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    /**
     * поиск токена по содеражнию
     * @param token содержание токена
     * @return токен подтверждения ввиде опционала
     */
    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}
