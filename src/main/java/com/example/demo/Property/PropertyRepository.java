package com.example.demo.Property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * репозиторий работы с характеристиками товаров
 * @author Maximus
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    /**
     * получение характеристики по id
     * @param id id характеристики
     * @return объект характеристики
     */
    Property findById(int id);
    /**
     * удаление характеристики по id
     * @param id id характеристики
     */
    void deleteById(Long id);
}
