package com.example.demo.Product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;


@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "picture_link", length = 100, nullable = false)
    private String link;

    @Column(name = "manufacturer", length = 100, nullable = false)
    private String manufacturer;

    @Column(name = "id_category", nullable = false)
    private int categoryId;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity", nullable = false)
    private int quantity;


}
