package com.example.demo.Product;

import com.example.demo.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);
    Product findById(int id);
    List<Product> findByCategoryId(int categoryId);
    List<Product> findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn(
            String filterName, int categoryId, int minPrice, int maxPrice, int minQuantity
            , int maxQuantity, List<String> manufactures_list, List<Integer> Rating);
    Long deleteById(Long id);
}
