package com.example.demo.Description;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "descriptions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="id_product", nullable = false)
    public int productid;

    @Column(name="description", length = 1000, nullable = false)
    public String description;

}

