package com.example.demo;

import com.example.demo.Property.Property;
import com.example.demo.Property.PropertyRepository;
import com.example.demo.Property.PropertyService;
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
public class PropertyServiceTests {
    @Mock
    private PropertyRepository propertyRepository;
    @Captor
    ArgumentCaptor<Property> captor;
    @Test
    void getProperties() {
        Property property1 = new Property();
        property1.setId(1);
        property1.setName("Length");
        property1.setValue("10 m");
        Property property2 = new Property();
        property1.setId(2);
        property1.setName("Width");
        property1.setValue("20 m");
        Mockito.when(propertyRepository.findAll()).thenReturn(List.of(property1,
                property2));
        PropertyService ps = new PropertyService(propertyRepository);
        assertEquals(2,
                ps.readAll().size());
        assertEquals("Width",
                propertyRepository.findAll().get(0).getName());
    }
    @Test
    void PropertyFindById() {
        Property property1 = new Property();
        property1.setId(1);
        property1.setName("Length");
        property1.setValue("10 m");
        PropertyService ps = new PropertyService(propertyRepository);
        Mockito.when(propertyRepository.findById(1)).thenReturn(property1);
        assertEquals("Length",
                ps.findById(1).getName());
    }
    @Test
    void PropertyDelete() {
        Property property1 = new Property();
        property1.setId(1);
        property1.setName("Length");
        property1.setValue("10 m");
        PropertyService ps = new PropertyService(propertyRepository);
        Mockito.when(propertyRepository.findById(1)).thenReturn(property1);
        assertEquals("Length",
                ps.findById(1).getName());
        ps.delete(property1);
        Mockito.verify(propertyRepository).deleteById(1);
        assertEquals(0,ps.readAll().size());
    }
}
