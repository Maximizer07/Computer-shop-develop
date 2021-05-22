package com.example.demo.Property;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * сервис работы с характеристиками товаров
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class PropertyService {
    /**
     * репозиторий работы с характеристиками товаров
     */
    @Autowired
    private PropertyRepository propertyRepository;
    /**
     * сохранение харакеристики
     * @param property объект харакеристики
     */
    public void save(Property property){
        propertyRepository.save(property);
    }
    /**
     * удаление харакеристики
     * @param p объект харакеристики
     */
    public void delete(Property p){
        propertyRepository.deleteById(p.getId());
    }
}
