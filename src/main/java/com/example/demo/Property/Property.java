package com.example.demo.Property;
import com.example.demo.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "properties")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="property", length = 100, nullable = false)
    public String name;


    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @Column(name="value", length = 100, nullable = false)
    private String value;
}
