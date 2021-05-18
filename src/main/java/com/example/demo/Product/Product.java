package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Property.Property;
import com.example.demo.Review.Review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "products", schema = "public")
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


    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "quantity", nullable = false)
    private int quantity;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Property> properties;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;
}
