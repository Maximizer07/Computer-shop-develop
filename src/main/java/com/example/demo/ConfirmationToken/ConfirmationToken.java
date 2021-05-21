package com.example.demo.ConfirmationToken;

import com.example.demo.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * сущность Токен подтверждения акканута
 * @author mike
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationToken {

    /**
     * id токена
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * содержание токена
     */
    private String confirmationToken;

    /**
     * дата создания токена
     */
    private LocalDate createdDate;

    /**
     * пользователь, для которого создается токен подтверждения
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    /**
     * конструктор
     * @param user пользователь
     */
    public ConfirmationToken(User user) {
        this.user = user;
        this.createdDate = LocalDate.now();
        this.confirmationToken = UUID.randomUUID().toString();
    }
}
