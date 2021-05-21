package com.example.demo.Order;

import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Product.Product;
import com.example.demo.ShoppingCart.Shopping_cart;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * сущность заказа
 * @author mike
 */
@Entity
@Getter
@Setter
@ToString
@Data
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    /**
     * id заказа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * номер заказа
     */
    @Column(name = "number")
    private String number;
    /**
     * дата создания заказа
     */
    @JsonFormat(pattern="MM/dd/yyyy")
    @Column(name = "dateset")
    private LocalDate dateSet;
    /**
     * дата получения  заказа
     */
    @JsonFormat(pattern="MM/dd/yyyy")
    @Column(name = "dateget")
    private LocalDate dateGet;
    /**
     * цена заказа
     */
    @Column(name = "prise")
    private double prise;
    /**
     * статус заказа
     */
    @Column(name = "status")
    private int status;
    /**
     * id заказчика
     */
    @Column(name = "userid")
    private Long userid;
    /**
     * продукты заказа
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<Cart_Item> products;
}
