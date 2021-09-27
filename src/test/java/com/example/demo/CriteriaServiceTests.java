package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryRepository;
import com.example.demo.Category.CategoryService;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Product.ProductService;
import com.example.demo.Review.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CriteriaServiceTests {
    @InjectMocks
    private ProductService productService;
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CriteriaService criteriaService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Captor
    ArgumentCaptor<Product> productCaptor;
    @Captor
    ArgumentCaptor<Category> categoryCaptor;

    private Product product1, product2;
    private Category category1, category2;
    @BeforeEach
    void init() {
        product1 = new Product();
        product1.setName("Intel1");
        product1.setId(3);
        product1.setQuantity(5);
        product1.setPrice(1000);
        product1.setLink("test");
        product1.setManufacturer("Intel");
        product1.setRating(5);

        product2 = new Product();
        product2.setName("Intel5");
        product2.setId(4);
        product2.setQuantity(5);
        product2.setPrice(2000);
        product2.setLink("test");
        product2.setManufacturer("Intel");
        product2.setRating(5);


        category1 = new Category();
        category1.setName("Видеокарты");
        category1.setId(1);
        category2 = new Category();
        category2.setName("Процессоры");
        category2.setId(2);
        product1.setCategory(category2);
    }

    @Test
    void takeCategories() {
        categoryService.create(category1);
        Mockito.verify(categoryRepository).save(categoryCaptor.capture());

        Mockito.when(criteriaService.takeCategories("Intel1",2)).thenReturn(List.of(category2));
        assertEquals(List.of(category2), criteriaService.takeCategories("Intel1",2));
    }

    @Test
    void takeProducts() {
        productService.create(product1);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Mockito.when(criteriaService.takeProducts("Intel",null,null,null,null)).thenReturn(List.of(product1));
        assertEquals(List.of(product1), criteriaService.takeProducts("Intel",null,null,null,null));
    }

    @Test
    void takeProductList() {
        productService.create(product2);
        Mockito.verify(productRepository).save(productCaptor.capture());

        Mockito.when(criteriaService.takeProductList(null,null,2,null,null,10,20000,null)).thenReturn(List.of(product2));
        assertEquals(List.of(product2), criteriaService.takeProductList(null,null,2,null,null,10,20000,null));
    }

}
