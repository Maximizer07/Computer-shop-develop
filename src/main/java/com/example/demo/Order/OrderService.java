package com.example.demo.Order;

import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * сервис работы с заказами
 * @author mike
 */
@Service
@Transactional
public class OrderService {
    /**
     * репозиторий заказов
     */
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartItemService cartItemService;

    /**
     * конструктор заказов
     * @param orderRepository репозиторий заказов
     */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * сохранение заказа
     * @param o заказ
     */
    public void save(Order o){
        orderRepository.save(o);
    }

    /**
     * получение всех заказов
     * @return список заказов
     */
    public List<Order> readAll() {
        return orderRepository.findAll();
    }

    /**
     * получение всех заказов пользователя
     * @param id id пользователя
     * @return список товаров
     */
    public List<Order> findbyuser(long id){
        return orderRepository.findByUserid(id);
    }

    /**
     * поиск определенного заказа по номеру
     * @param number номер заказа
     * @return заказ
     */
    public Order findbynumber(String number){
        return orderRepository.findByNumber(number);
    }

    /**
     * удаление заказа
     * @param number номер заказа
     */
    public void delete(String number){
        if(orderRepository.findByNumber(number).getProducts()!=null) {
            for (Cart_Item cart_item : orderRepository.findByNumber(number).getProducts()) {
                cartItemService.delete(cart_item);
            }
        }
        orderRepository.deleteByNumber(number);
    }
}
