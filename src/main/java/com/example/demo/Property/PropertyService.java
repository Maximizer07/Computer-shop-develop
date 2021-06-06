package com.example.demo.Property;

import com.example.demo.Description.Description;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
     * конструктор сервиса
     * @param propertyRepository репозиторий работы с характеристиками товаров
     */
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    /**
     * получение характеристики по id
     * @param Id id характеристики
     * @return объект характеристики
     */
    public Property findById(int Id){
        log.info("Find Property, whose Id = {}",Id);
        return propertyRepository.findById(Id);
    }
    /**
     * получение всех характеристик
     * @return список всех характеристик
     */
    public List<Property> readAll() {
        log.info("Find all properties");
        return propertyRepository.findAll();
    }
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
