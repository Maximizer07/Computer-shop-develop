package com.example.demo.Property;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    public void save(Property property){
        propertyRepository.save(property);
    }

    public void delete(Property p){
        propertyRepository.deleteById(p.getId());
    }
}
