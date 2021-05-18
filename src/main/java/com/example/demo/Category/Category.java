package com.example.demo.Category;
import com.example.demo.Product.Product;
import com.example.demo.Property.Property;
import com.example.demo.Review.Review;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ibm.icu.text.Transliterator;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Data
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", length = 100, nullable = false)
    public String name;

    @Column(name="eng_name", length = 100, nullable = false)
    public String engname;
    @Column(name="picture_link", length = 100, nullable = false)
    private String link;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;

    public String EngName() {
        var CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        String result = toLatinTrans.transliterate(name);
        return result.replaceAll("\\s+", "-").toLowerCase();
    }
}
