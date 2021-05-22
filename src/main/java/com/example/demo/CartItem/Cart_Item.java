package com.example.demo.CartItem;

import com.example.demo.Order.Order;
import com.example.demo.Product.Product;
import com.example.demo.ShoppingCart.Shopping_cart;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

/**
 * сущность продукта в корзине
 * @author mike
 */
@Table(name = "cart_items")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cart_Item {
    /**
     * id продукта в корзине
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * связь много к одному с продуктом в каталоге
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    /**
     * связь много к одному с заказом
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    /**
     * количество элементов товара в корзине
     */
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