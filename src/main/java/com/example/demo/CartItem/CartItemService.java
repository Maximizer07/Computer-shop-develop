package com.example.demo.CartItem;

import com.example.demo.Product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
