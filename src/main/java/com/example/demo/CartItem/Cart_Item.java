package com.example.demo.CartItem;

import com.example.demo.Product.Product;
import com.example.demo.ShoppingCart.Shopping_cart;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Table(name = "cart_items")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cart_Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private Shopping_cart shopping_cart;
    @Column(name = "quantity")
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cart_Item cart_item = (Cart_Item) o;

        return id != null && id.equals(cart_item.id);
    }

    @Override
    public int hashCode() {
        return 92470243;
    }
}