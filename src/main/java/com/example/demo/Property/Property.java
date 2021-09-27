package com.example.demo.Property;
import com.example.demo.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;

/**
 * Класс представляющий модель характеристики продукта
 * @author Maximus
 */
@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "properties")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Property {
    /**
     * Идентификатор характеристики
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * Название характеристики
     */
    @Column(name="property", length = 100, nullable = false)
    public String name;
    /**
     * Связь много к одному с продуктом
     */
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    /**
     * Значение характеристики
     */
    @Column(name="value", length = 100, nullable = false)
    private String value;
}
