package com.example.demo.CartItem;

import com.example.demo.Product.Product;
import com.example.demo.User.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }
    public void create(Cart_Item cart_item) {
        log.info("Save product {}", cart_item);
        cartItemRepository.save(cart_item);
    }
    public List<Cart_Item> readAll() {
        return cartItemRepository.findAll();
    }
    public List<Cart_Item> findbyid(int number){
        return cartItemRepository.findByProduct_Id(number);
    }
    public void delete(Cart_Item cart_item){
        cartItemRepository.delete(cart_item);
    }
}
