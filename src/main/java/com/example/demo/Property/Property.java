package com.example.demo.Property;
import com.example.demo.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;

/**
 * сущность характеристика продукта
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
     * id характеристики
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * название характеристики
     */
    @Column(name="property", length = 100, nullable = false)
    public String name;
    /**
     * связь много к одному с продуктом
     */
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;
    /**
     * значение характеристики
     */
    @Column(name="value", length = 100, nullable = false)
    private String value;
}
