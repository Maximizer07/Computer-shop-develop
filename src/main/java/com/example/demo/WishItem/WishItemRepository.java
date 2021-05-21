package com.example.demo.WishItem;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * репозиторий работы с товарами в списке желаемых
 * @author mike
 */
@Repository
public interface WishItemRepository extends JpaRepository<WishItem, Long> {
    /**
     * поиск товара  в списке по id
     * @param id id товара в списке
     * @return спискок товаров
     */
    List<WishItem> findById(long id);
}