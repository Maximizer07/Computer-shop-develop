package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;
    @Captor
    ArgumentCaptor<Product> captor;
    @Test
    void getProducts() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setId(1);
        Product product2 = new Product();
        product2.setName("Intel2");
        product1.setId(2);
        Mockito.when(productRepository.findAll()).thenReturn(List.of(product1,
                product2));
        ProductService ps =new ProductService(productRepository);
        assertEquals(2,
                ps.readAll().size());
        assertEquals("Intel1",
                productRepository.findAll().get(0).getName());
    }
    @Test
    void ProductFindByName() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setId(1);
        ProductService ps =new ProductService(productRepository);
        Mockito.when(productRepository.findByName("Intel1")).thenReturn(product1);
        assertEquals("Intel1",
                ps.findByName("Intel1").getName());
    }
    @Test
    void ProductFindById() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setId(1);
        ProductService ps =new ProductService(productRepository);
        Mockito.when(productRepository.findById(1)).thenReturn(product1);
        assertEquals("Intel1",
                ps.findById(1).getName());
    }
    @Test
    void ProductFindByIdCategory() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setId(1);
        Category category = new Category();
        category.setName("Процессоры");
        category.setId(1);
        product1.setCategory(category);
        ProductService ps =new ProductService(productRepository);
        Mockito.when(productRepository.findByCategoryId(1)).thenReturn(List.of(product1));
        assertEquals("Intel1",
                ps.findById_category(1).get(0).getName());
    }
    @Test
    void updateProduct() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setPrice(1000);
        product1.setQuantity(1);
        product1.setId(1);
        ProductService ps =new ProductService(productRepository);
        Mockito.when(productRepository.findById(1)).thenReturn(product1);
        assertEquals("Intel1",
                ps.findById(1).getName());
        ps.findById(1).setName("AMD2");
        assertEquals("AMD2",
                ps.findById(1).getName());
    }
    @Test
    void findByFilter() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setId(1);
        product1.setQuantity(5);
        product1.setPrice(1000);
        product1.setLink("test");
        product1.setManufacturer("Intel");
        product1.setRating(5);
        Category category = new Category();
        category.setName("Процессоры");
        category.setId(1);
        product1.setCategory(category);
        ProductService ps =new ProductService(productRepository);
        Mockito.when(productRepository.
                findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn
                        ("Intel", 1, 0,20000, 0 ,
                                10, List.of("Intel"),List.of(1,2,3,4,5))).thenReturn(List.of(product1));
        assertEquals("Intel1",
                productRepository.findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn
                        ("Intel", 1, 0,20000, 0 ,
                                10, List.of("Intel"),List.of(1,2,3,4,5)).get(0).getName());
        assertEquals(Collections.emptyList(),
                productRepository.findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn
                        ("Intel", 2, 1000,20000, 0 ,
                                10, List.of("Intel"),List.of(1,2,3,4,5)));
    }
    @Test
    void productDelete() {
        Product product1 = new Product();
        product1.setName("Intel1");
        product1.setId(1);
        ProductService ps =new ProductService(productRepository);
        Mockito.when(productRepository.findById(1)).thenReturn(product1);
        assertEquals("Intel1",
                ps.findById(1).getName());
        ps.delete(product1);
        Mockito.verify(productRepository).deleteById(1);
        assertEquals(0,ps.readAll().size());
    }
}
