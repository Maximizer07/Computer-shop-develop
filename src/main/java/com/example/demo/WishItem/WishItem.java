package com.example.demo.WishItem;

import com.example.demo.Product.Product;
import com.example.demo.User.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

/**
 * сущность товара в списке желемых
 * @author mike
 */
@Table(name = "wish_items")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WishItem {
    /**
     * id товара в списке
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * продукт в каталоге
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    /**
     * пользователь
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WishItem wishItem = (WishItem) o;

        return id != null && id.equals(wishItem.id);
    }

    @Override
    public int hashCode() {
        return 1150339719;
    }
}