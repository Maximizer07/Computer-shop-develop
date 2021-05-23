package com.example.demo;

import com.example.demo.Description.Description;
import com.example.demo.Description.DescriptionRepository;
import com.example.demo.Description.DescriptionService;
import com.example.demo.Product.Product;
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
public class DescriptionServiceTests {
    @Mock
    private DescriptionRepository descriptionRepository;
    @Captor
    ArgumentCaptor<Product> captor;
    @Test
    void getDescriptions() {
        Description description1 = new Description();
        description1.setId(1);
        description1.setDescription("Test1");
        Description description2 = new Description();
        description1.setId(2);
        description1.setDescription("Test2");
        Mockito.when(descriptionRepository.findAll()).thenReturn(List.of(description1,
                description2));
        DescriptionService ds =new DescriptionService(descriptionRepository);
        assertEquals(2,
                ds.readAll().size());
        assertEquals("Test2",
                descriptionRepository.findAll().get(0).getDescription());
    }
    @Test
    void ProductFindById() {
        Description description1 = new Description();
        description1.setId(1);
        description1.setDescription("Test1");
        DescriptionService ds =new DescriptionService(descriptionRepository);
        Mockito.when(descriptionRepository.findById(1)).thenReturn(description1);
        assertEquals("Test1",
                ds.findById(1).getDescription());
    }
    @Test
    void ProductDelete() {
        Description description1 = new Description();
        description1.setId(1);
        description1.setDescription("Test1");
        DescriptionService ds =new DescriptionService(descriptionRepository);
        Mockito.when(descriptionRepository.findById(1)).thenReturn(description1);
        assertEquals("Test1",
                ds.findById(1).getDescription());
        ds.delete(description1);
        Mockito.verify(descriptionRepository).deleteById(1);
        assertEquals(0,ds.readAll().size());
    }

}
