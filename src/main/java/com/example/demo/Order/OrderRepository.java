package com.example.demo.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * репозиторий работы с заказами
 */
@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    /**
     * получение всех заказов по id пользователя
     * @param id id пользователя
     * @return список заказов
     */
    List<Order> findByUserid(Long id);

    /**
     * поиск заказа по номеру
     * @param number номер заказа
     * @return заказ
     */
    Order findByNumber(String number);

    /**
     * удаление заказа по номеру
     * @param number номер заказа
     */
    void deleteByNumber(String number);
}
