package com.example.demo.Description;
import com.example.demo.Category.Category;
import com.example.demo.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;

/**
 * Класс представляющий модель описания продукта
 * @author Maximus
 */
@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "descriptions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Description {
    /**
     * Идентификатор описания
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Идентификатор продукта, чье описание
     */
    @OneToOne(
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "id_product")
    private Product product;
    /**
     * Описание товара
     */
    @Column(name="description", length = 1000, nullable = false)
    public String description;

}

