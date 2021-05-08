package com.example.demo.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);
    List<Product> findProductsById_category(int id_category);
    Long deleteById(Long id);
}
