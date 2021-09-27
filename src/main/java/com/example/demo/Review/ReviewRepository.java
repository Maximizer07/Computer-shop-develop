package com.example.demo.Review;

import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Description.Description;
import com.example.demo.WishItem.WishItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с отзывами товаров из таблицы БД
 * @author Maximus
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * Метод удаляющий отзыв по идентификатору
     * @param id Идентификатор отзыва
     */
    void deleteById(Long id);

    /**
     * Метод получающий отзыв по идентификатору
     * @param id Идентификатор отзыва
     * @return Искомый отзыв
     */
    Review findById(int id);
}