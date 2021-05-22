package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Description.Description;
import com.example.demo.Property.Property;
import com.example.demo.Review.Review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.util.List;

/**
 * сущность продукт
 * @author Maximus
 */
@Getter
@Setter
@Entity
@Data
@Table(name = "products", schema = "public")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    /**
     * id продукта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * название товара
     */
    @Column(name = "name", length = 100, nullable = false)
    public String name;
    /**
     * ссылка на изображение
     */
    @Column(name = "picture_link", length = 100, nullable = false)
    private String link;
    /**
     * производитель продукта
     */
    @Column(name = "manufacturer", length = 100, nullable = false)
    private String manufacturer;
    /**
     * цена продукта
     */
    @Column(name = "price", nullable = true)
    private Integer price;
    /**
     * рейтинг продукта
     */
    @Column(name = "rating", nullable = false)
    private int rating;
    /**
     * количество продукта
     */
    @Column(name = "quantity", nullable = true)
    private int quantity;
    /**
     * обзоры продукта
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews;
    /**
     * характеристики продукта
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Property> properties;

    /**
     * связь много к одному с категорией
     */
    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;
    /**
     * id описания продукта
     */
    @OneToOne(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private Description description;
    /**
     * получение строкового предстваления сущности
     * @return строка полей товара
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", quantity=" + quantity +
                '}';
    }
}
