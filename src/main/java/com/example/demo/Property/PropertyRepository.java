package com.example.demo.Property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий работы с характеристиками товаров из таблицы БД
 * @author Maximus
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    /**
     * Метод получает характеристику по идентификатору
     * @param id Идентификатор характеристики
     * @return Искомая характеристика
     */
    Property findById(int id);
    /**
     * Метод удаляет характеристику по идентификатору
     * @param id Идентификатор характеристики
     */
    void deleteById(Long id);
}
