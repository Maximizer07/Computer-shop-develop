package com.example.demo.ShoppingCart;

import com.example.demo.CartItem.CartItemRepository;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingcartRepository shoppingcartRepository;

    public ShoppingCartService(ShoppingcartRepository shoppingcartRepository) {
        this.shoppingcartRepository = shoppingcartRepository;
    }

    public Shopping_cart findbyuser(long id){
        return shoppingcartRepository.findByUserid(id);
    }
    public void create(Shopping_cart shopping_cart) {
        shoppingcartRepository.save(shopping_cart);
    }

}
