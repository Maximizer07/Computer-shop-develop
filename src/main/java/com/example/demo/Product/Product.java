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
 * Класс представляющий модель продукта
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
     * Идентификатор продукта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Название товара
     */
    @Column(name = "name", length = 100, nullable = false)
    public String name;
    /**
     * Ссылка на изображение
     */
    @Column(name = "picture_link", length = 100, nullable = false)
    private String link;
    /**
     * Производитель продукта
     */
    @Column(name = "manufacturer", length = 100, nullable = false)
    private String manufacturer;
    /**
     * Цена продукта
     */
    @Column(name = "price", nullable = true)
    private Integer price;
    /**
     * Рейтинг продукта
     */
    @Column(name = "rating", nullable = false)
    private int rating;
    /**
     * Количество продукта
     */
    @Column(name = "quantity", nullable = true)
    private int quantity;
    /**
     * Обзоры продукта
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews;
    /**
     * Характеристики продукта
     */
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Property> properties;

    /**
     * Связь много к одному с категорией
     */
    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;
    /**
     * Идентификатор описания продукта
     */
    @OneToOne(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "description_id")
    private Description description;
    /**
     * Получение строкового предстваления сущности
     * @return Строка со значениями полей продукта
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
