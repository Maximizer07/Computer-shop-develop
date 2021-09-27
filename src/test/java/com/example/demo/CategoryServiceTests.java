package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryService;
import com.example.demo.Category.CategoryRepository;
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
        Category category1 = new Category();
        category1.setName("category1");
        category1.setEngname("category1");
        category1.setId(1);
        Category category2 = new Category();
        category2.setName("category2");
        category2.setId(2);
    }
    @Test
    void getCategories() {
        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1,
                category2));
        assertEquals(2,
                categoryRepository.findAll().size());
    }

    @Test
    void CategoryFindByName() {
        Mockito.when(categoryRepository.findByName("category1")).thenReturn(category1);
        assertEquals("category1",
                categoryService.findByName("category1").getName());
    }
    @Test
    void CategoryFindByEngName() {
        Mockito.when(categoryRepository.findByEngname("category1")).thenReturn(category1);
        assertEquals("category1",
                categoryService.findByEngname("category1").getEngname());
    }
    @Test
    void CategoryFindById() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        assertEquals("category1",
                categoryService.findById(1).getName());
    }
    @Test
    void categoryDeleteById() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        assertEquals("category1",
                categoryService.findById(1).getName());
        categoryService.delete(category1);
        Mockito.verify(categoryRepository).deleteById(1);
        assertEquals(0,categoryService.readAll().size());
    }
    @Test
    void categoryDeleteByNameAndLink() {
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        assertEquals("category1",
                categoryService.findById(1).getName());
        categoryService.del(category1);
        Mockito.verify(categoryRepository).deleteByNameAndAndLink("category1", "link");
        assertEquals(0,categoryService.readAll().size());
    }

}
