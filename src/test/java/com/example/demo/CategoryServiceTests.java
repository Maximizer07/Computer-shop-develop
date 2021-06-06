package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryService;
import com.example.demo.Category.CategoryRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;
    @Captor
    ArgumentCaptor<Category> captor;
    @Test
    void getCategories() {
        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1);
        Category category2 = new Category();
        category2.setName("category2");
        category2.setId(2);
        CategoryService cs =new CategoryService(categoryRepository);
        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1,
                category2));
        assertEquals(2,
                cs.readAll().size());
        assertEquals("category1",
                categoryRepository.findAll().get(0).getName());
    }

    @Test
    void CategoryFindByName() {
        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1);
        CategoryService cs =new CategoryService(categoryRepository);
        Mockito.when(categoryRepository.findByName("category1")).thenReturn(category1);
        assertEquals("category1",
                cs.findByName("category1").getName());
    }
    @Test
    void CategoryFindByEngName() {
        Category category1 = new Category();
        category1.setEngname("category1");
        category1.setId(1);
        CategoryService cs =new CategoryService(categoryRepository);
        Mockito.when(categoryRepository.findByEngname("category1")).thenReturn(category1);
        assertEquals("category1",
                cs.findByEngname("category1").getEngname());
    }
    @Test
    void CategoryFindById() {
        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1);
        CategoryService cs =new CategoryService(categoryRepository);
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        assertEquals("category1",
                cs.findById(1).getName());
    }
    @Test
    void categoryDeleteById() {
        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1);
        CategoryService cs =new CategoryService(categoryRepository);
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        assertEquals("category1",
                cs.findById(1).getName());
        cs.delete(category1);
        Mockito.verify(categoryRepository).deleteById(1);
        assertEquals(0,cs.readAll().size());
    }
    @Test

    void categoryDeleteByNameAndLink() {
        Category category1 = new Category();
        category1.setName("category1");
        category1.setLink("link");
        category1.setId(1);
        CategoryService cs =new CategoryService(categoryRepository);
        Mockito.when(categoryRepository.findById(1)).thenReturn(category1);
        assertEquals("category1",
                cs.findById(1).getName());
        cs.del(category1);
        Mockito.verify(categoryRepository).deleteByNameAndAndLink("category1", "link");
        assertEquals(0,cs.readAll().size());
    }

}
