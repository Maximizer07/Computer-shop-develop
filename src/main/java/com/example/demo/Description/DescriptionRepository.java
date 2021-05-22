package com.example.demo.Description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * репозиторий работы с описаниями товаров
 * @author Maximus
 */
@Repository
public interface DescriptionRepository extends JpaRepository<Description, Integer> {
    /**
     * получение описания по id
     * @param Id id описания
     * @return объект описания
     */
    Description findById(int Id);
}
