package com.example.demo.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * репозиторий работы с пользователями
 * @author mike
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * @param email почта пользователя
     * @return пользователь в виде опционала
     */
    Optional<User> findByEmail(String email);

    /**
     * @param id  id пользователя
     * @return пользователь в виде опционала
     */
    Optional<User> findById(Long id);

    /**
     * @param id удаление пользователя по id
     */
    void deleteUserById(Long id);
}