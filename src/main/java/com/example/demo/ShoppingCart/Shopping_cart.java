package com.example.demo.ShoppingCart;

import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@Table(name = "shopping_carts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shopping_cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userid")
    private Long userid;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Shopping_cart that = (Shopping_cart) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 1534474956;
    }
}
