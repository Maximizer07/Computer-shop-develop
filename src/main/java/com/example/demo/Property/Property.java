package com.example.demo.Property;
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

    @Column(name = "id_product", nullable = false)
    private int productId;
    @Column(name="value", length = 100, nullable = false)
    private String value;
}
