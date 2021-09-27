package com.example.demo;

import com.example.demo.Description.Description;
import com.example.demo.Description.DescriptionRepository;
import com.example.demo.Description.DescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DescriptionServiceTests {
    @InjectMocks
    private DescriptionService descriptionService;
    @Mock
    private DescriptionRepository descriptionRepository;
    @Captor
    ArgumentCaptor<Description> captor;

    private Description description1, description2;
    @BeforeEach
    void init() {
        description1 = new Description();
        description1.setId(1);
        description1.setDescription("Test1");
        description2 = new Description();
        description2.setId(2);
        description2.setDescription("Test2");
    }
    @Test
    void addDescription() {
        descriptionService.create(description1);
        Mockito.verify(descriptionRepository).save(captor.capture());
        Description captured = captor.getValue();
        assertEquals(description1, captured);
    }
    @Test
    void getDescriptions() {
        Mockito.when(descriptionRepository.findAll()).thenReturn(List.of(description1, description2));
        assertEquals(List.of(description1, description2), descriptionRepository.findAll());
    }
    @Test
    void DescriptionFindById() {
        Mockito.when(descriptionRepository.findById(1)).thenReturn(description1);
        assertEquals(description1, descriptionRepository.findById(1));
    }
    @Test
    void DescriptionDelete() {
        descriptionService.delete(description2);
        Mockito.verify(descriptionRepository).deleteById(2);
    }

}
