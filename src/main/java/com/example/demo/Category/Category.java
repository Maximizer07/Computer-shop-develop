package com.example.demo.Category;
import com.example.demo.Product.Product;
import com.example.demo.Property.Property;
import com.example.demo.Review.Review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * сущность категория продуктов
 * @author Maximus
 */
@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Category {
    /**
     * id категории
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * название категории
     */
    @Column(name="name", length = 100, nullable = false)
    public String name;
    /**
     * английское название категории
     */
    @Column(name="eng_name", length = 100, nullable = false)
    public String engname;
    /**
     * ссылка на изображение
     */
    @Column(name="picture_link", length = 100, nullable = false)
    private String link;
    /**
     * продукты категории
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;
}
