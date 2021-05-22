package com.example.demo.CartItem;

import com.example.demo.Product.Product;
import com.example.demo.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * сервис работы с продуктами в корзине
 * @author mike
 */
@Service
@Slf4j
@Transactional
public class CartItemService {
    /**
     * репозиторий работы с товарами в корзине
     */
    @Autowired
    private final CartItemRepository cartItemRepository;

    /**
     * конструктор сервиса
     * @param cartItemRepository репозиторий работы с товарами в корзине
     */
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * добавление проудкта в корзину
     * @param cart_item элемент корзины
     */
    public void create(Cart_Item cart_item) {
        cartItemRepository.save(cart_item);
    }

    /**
     * получение товаров корзине
     * @return список всех товаров в корзине
     */
    public List<Cart_Item> readAll() {
        return cartItemRepository.findAll();
    }

    /**
     * @param number id продукта корзины
     * @return список товаров корзины по id продукта
     */
    public List<Cart_Item> findbyid(int number){
        return cartItemRepository.findByProduct_Id(number);
    }

    /**
     * удаление элемента корзины
     * @param cart_item элемент корзины
     */
    public void delete(Cart_Item cart_item){
        cartItemRepository.delete(cart_item);
    }
}
