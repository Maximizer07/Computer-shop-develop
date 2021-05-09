package com.example.demo.Order;

import com.example.demo.Product.Product;
import com.example.demo.ShoppingCart.Shopping_cart;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Data
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number")
    private String number;
    @JsonFormat(pattern="MM/dd/yyyy")
    @Column(name = "dateset")
    private LocalDate dateSet;
    @JsonFormat(pattern="MM/dd/yyyy")
    @Column(name = "dateget")
    private LocalDate dateGet;
    @Column(name = "prise")
    private double prise;
    @Column(name = "status")
    private int status;
    @Column(name = "userid")
    private Long userid;
    @OneToOne
    @JoinColumn(name = "shopping_cart_id")
    private Shopping_cart shopping_cart;
}
