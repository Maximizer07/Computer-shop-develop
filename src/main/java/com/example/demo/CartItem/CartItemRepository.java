package com.example.demo.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * репозиторий работы с продуктами в корзине
 * @author mike
 */
@Repository
public interface CartItemRepository extends JpaRepository<Cart_Item, Long> {
    /**
     * @param number id продукта
     * @return список товаров
     */
    List<Cart_Item> findByProduct_Id(int number);
}
