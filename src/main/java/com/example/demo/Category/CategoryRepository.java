package com.example.demo.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * репозиторий работы с категориями товаров
 * @author Maximus
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    /**
     * получение категории по названию
     * @param name название категории
     * @return объект категории
     */
    Category findByName(String name);
    /**
     * получение категории по id
     * @param Id id категории
     * @return объект категории
     */
    Category findById(int Id);
    /**
     * получение категории по английскому названию
     * @param name английское название категории
     * @return объект категории
     */
    Category findByEngname(String name);
    /**
     * получение всех категорий по id и названию категории
     * @param id id категории
     * @param Name название категории
     * @return список категорий
     */
    List<Category> findByIdAndName(int id, String Name);
    /**
     * удаление категории по id
     * @param id id категории
     */
    void deleteById(Integer id);
    /**
     * удаление категории по названию и ссылке
     * @param name название категории
     * @param Link ссылка на изображение
     */
    void deleteByNameAndAndLink(String name, String Link);
}
