package com.example.demo;

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
