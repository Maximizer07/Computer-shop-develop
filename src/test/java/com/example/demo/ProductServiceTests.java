package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @InjectMocks
    ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Captor
    ArgumentCaptor<Product> captor;

    Product product1, product2, product3;
    @BeforeEach
    void init() {
        product1 = new Product();
        product1.setName("Intel");
        product1.setId(1);
        product2 = new Product();
        product2.setName("AMD");
        product2.setId(2);

        product3 = new Product();
        product3.setName("Intel1");
        product3.setId(3);
        product3.setQuantity(5);
        product3.setPrice(1000);
        product3.setLink("test");
        product3.setManufacturer("Intel");
        product3.setRating(5);
        Category category2 = new Category();
        category2.setName("Процессоры");
        category2.setId(1);
        product3.setCategory(category2);
    }
    @Test
    void getProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        assertEquals(List.of(product1, product2), productRepository.findAll());
    }
    @Test
    void addProduct() {
        productService.create(product2);
        Mockito.verify(productRepository).save(captor.capture());
        Product captured = captor.getValue();
        assertEquals(product2, captured);
    }
    @Test
    void ProductFind() {
        Mockito.when(productRepository.findById(1)).thenReturn(product1);
        Mockito.when(productRepository.findByName("AMD")).thenReturn(product2);
        Mockito.when(productRepository.findByCategoryId(1)).thenReturn(List.of(product3));
        assertEquals(product1, productRepository.findById(1));
        assertEquals(product2, productRepository.findByName("AMD"));
        assertEquals(List.of(product3), productRepository.findByCategoryId(1));
    }
    @Test
    void findByFilter() {
        Mockito.when(productRepository.
                findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn
                        ("Intel", 1, 0,20000, 0 ,
                                10, List.of("Intel"),List.of(1,2,3,4,5))).thenReturn(List.of(product1));
        assertEquals(List.of(product1),
                productRepository.findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn
                        ("Intel", 1, 0,20000, 0 ,
                                10, List.of("Intel"),List.of(1,2,3,4,5)));
        assertEquals(Collections.emptyList(),
                productRepository.findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn
                        ("Intel", 2, 1000,20000, 0 ,
                                10, List.of("Intel"),List.of(1,2,3,4,5)));
    }
    @Test
    void productDelete() {
        productService.delete(product3);
        Mockito.verify(productRepository).deleteById(3);
    }
}
