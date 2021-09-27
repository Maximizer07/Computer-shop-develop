package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryService;
import com.example.demo.Category.CategoryRepository;
import com.example.demo.Product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    @Captor
    ArgumentCaptor<Category> captor;

    private Category category1, category2, category3;
    @BeforeEach
    void init() {
        category1 = new Category();
        category1.setName("category1");
        category1.setEngname("engname");
        category1.setId(1);
        category1.setLink("www");

        category2 = new Category();
        category2.setName("category2");
        category2.setId(2);
    }
    @Test
    void getCategories() {
        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        assertEquals(List.of(category1, category2), categoryRepository.findAll());
    }
    @Test
    void addCategory() {
        categoryService.create(category1);
        Mockito.verify(categoryRepository).save(captor.capture());
        Category captured = captor.getValue();
        assertEquals(category1, captured);
    }
    @Test
    void ProductFind() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        Mockito.when(categoryRepository.findByName("category2")).thenReturn(category2);
        Mockito.when(categoryRepository.findByEngname("engname")).thenReturn(category1);
        assertEquals(category1, categoryRepository.findById(1));
        assertEquals(category2, categoryRepository.findByName("category2"));
        assertEquals(category1, categoryRepository.findByEngname("engname"));
    }

    @Test
    void categoryDeleteById() {
        categoryService.delete(category1);
        Mockito.verify(categoryRepository).deleteById(1);
    }
    @Test
    void categoryDeleteByNameAndLink() {
        categoryService.del(category1);
        Mockito.verify(categoryRepository).deleteByNameAndAndLink("category1","www");
    }
}
