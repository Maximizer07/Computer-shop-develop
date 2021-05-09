package com.example.demo.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);
    Product findById(int id);
    List<Product> findByCategoryId(int categoryId);
    Long deleteById(Long id);
}
