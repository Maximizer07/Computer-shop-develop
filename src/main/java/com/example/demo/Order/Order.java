package com.example.demo.Order;

import com.example.demo.Product.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
public class Order {
    private int number;
    private String dateSet;
    private String dateGet;
    private double prise;
    private List<Product> productList;
    private int status;
}
