package com.example.demo.Description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с описаниями товаров из таблицы БД
 * @author Maximus
 */
@Repository
public interface DescriptionRepository extends JpaRepository<Description, Integer> {
    /**
     * Метод получает описание по идентификатору
     * @param Id Идентификатор описания
     * @return Искомое описание товара
     */
    Description findById(int Id);
}
