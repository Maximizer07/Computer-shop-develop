package com.example.demo.Property;

import com.example.demo.Description.Description;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с характеристиками товаров из таблицы БД
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class PropertyService {
    /**
     * Репозиторий работы с характеристиками товаров из таблицы БД
     */
    @Autowired
    private PropertyRepository propertyRepository;
    /**
     * Конструктор сервиса для работы с характеристиками товаров из таблицы БД
     * @param propertyRepository Репозиторий работы с характеристиками товаров из таблицы БД
     */
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    /**
     * Метод получает характеристику по идентификатору
     * @param Id Идентификатор характеристики товара
     * @return Объект характеристики товара
     */
    public Property findById(int Id){
        log.info("Find Property, whose Id = {}",Id);
        return propertyRepository.findById(Id);
    }
    /**
     * Метод получает все характеристики товаров из БД
     * @return Список всех характеристик
     */
    public List<Property> readAll() {
        log.info("Find all properties");
        return propertyRepository.findAll();
    }
    /**
     * Метод сохраняет харакеристику в БД
     * @param property Объект харакеристики
     */
    public void save(Property property){
        propertyRepository.save(property);
    }
    /**
     * Метод удаляет харакеристику из БД
     * @param p Объект харакеристики
     */
    public void delete(Property p){
        propertyRepository.deleteById(p.getId());
    }
}
