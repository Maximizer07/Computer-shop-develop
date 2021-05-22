package com.example.demo.Review;

import com.example.demo.Product.Product;
import com.example.demo.User.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * сущность отзыв о продукте
 * @author Maximus
 */
@Table(name = "reviews")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Review {
    /**
     * id отзыва
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * комментарий отзыва
     */
    @Column(name = "comment", length = 1000)
    private String comment;
    /**
     * дата создания отзыва
     */
    @JsonFormat(pattern="MM/dd/yyyy")
    @Column(name = "created", nullable = true)
    private LocalDate created;
    /**
     * значение рейтинга отзыва
     */
    @Column(name = "rating", nullable = true)
    private int rating;
    /**
     * связь много к одному с продуктом
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    /**
     * связь много к одному с пользователем
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
