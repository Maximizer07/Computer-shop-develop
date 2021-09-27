package com.example.demo;

import com.example.demo.Property.Property;
import com.example.demo.Property.PropertyRepository;
import com.example.demo.Property.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTests {
    @InjectMocks
    private PropertyService propertyService;
    @Mock
    private PropertyRepository propertyRepository;
    @Captor
    ArgumentCaptor<Property> captor;

    private Property property1, property2;
    @BeforeEach
    void init() {
        property1 = new Property();
        property1.setId(1);
        property1.setName("Length");
        property1.setValue("10 m");
        property2 = new Property();
        property2.setId(2);
        property2.setName("Width");
        property2.setValue("20 m");
    }
    @Test
    void getProperties() {
        Mockito.when(propertyRepository.findAll()).thenReturn(List.of(property1, property2));
        assertEquals(List.of(property1, property2), propertyRepository.findAll());
    }
    @Test
    void addProperty() {
        propertyService.save(property1);
        Mockito.verify(propertyRepository).save(captor.capture());
        Property captured = captor.getValue();
        assertEquals(property1, captured);
    }
    @Test
    void PropertyFindById() {
        Mockito.when(propertyRepository.findById(1)).thenReturn(property1);
        assertEquals(property1, propertyRepository.findById(1));
    }
    @Test
    void PropertyDelete() {
        propertyService.delete(property2);
        Mockito.verify(propertyRepository).deleteById(2);
    }
}
