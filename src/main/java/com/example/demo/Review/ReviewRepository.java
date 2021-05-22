package com.example.demo.Review;

import com.example.demo.CartItem.Cart_Item;
import com.example.demo.WishItem.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * репозиторий работы с отзывами товаров
 * @author Maximus
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * удаление отзыва по id
     * @param id отзыва
     */
    void deleteById(Long id);
}