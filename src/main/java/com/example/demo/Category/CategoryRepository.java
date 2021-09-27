package com.example.demo.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с категориями товаров из таблицы БД
 * @author Maximus
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * Метод получает категорию по названию
     *
     * @param name Название категории
     * @return Искомая категория
     */
    Category findByName(String name);

    /**
     * Метод получает категорию по идентификатору
     *
     * @param Id Идентификатор категории
     * @return Искомая категория
     */
    Category findById(int Id);

    /**
     * Метод получает категорию по английскому названию
     *
     * @param name Английское название категории
     * @return Искомая категория
     */
    Category findByEngname(String name);

    /**
     * Метод получает список категорий по идентификатору и названию категории
     *
     * @param id Идентификатор категории
     * @param Name Название категории
     * @return Искомый список категорий
     */
    List<Category> findByIdAndName(int id, String Name);

    /**
     * Метод удаляет категорию по идентификатору
     *
     * @param id Идентификатор категории
     */
    void deleteById(Integer id);

    /**
     * Метод удаляет категорию по названию и ссылке
     *
     * @param name Название категории
     * @param Link Ссылка на изображение
     */
    void deleteByNameAndAndLink(String name, String Link);
}