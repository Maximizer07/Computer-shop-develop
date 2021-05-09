package com.example.demo.ShoppingCart;

import com.example.demo.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingcartRepository extends JpaRepository<Shopping_cart,Long> {
        Shopping_cart findByUserid(Long userid);
}
