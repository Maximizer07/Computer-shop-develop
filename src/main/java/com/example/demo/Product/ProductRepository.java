package com.example.demo.Product;

import com.example.demo.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
/**
 * Репозиторий для работы с продуктами из таблицы БД
 * @author Maximus
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /**
     * Метод получает продукт по названию
     * @param name Название продукта
     * @return Искомый продукт
     */
    Product findByName(String name);
    /**
     * Метод получает продукт по идентификатору
     * @param id Идентификатор продукта
     * @return Искомый продукт
     */
    Product findById(int id);
    /**
     * Метод получает список продуктов по идентификатору категории
     * @param categoryId Идентификатор категории
     * @return Искомый список продуктов
     */
    List<Product> findByCategoryId(int categoryId);
    /**
     * Метод производит поиск списка продуктов по определенному фильтру
     * @param filterName Фрагмент названия
     * @param categoryId Идентификатор категории
     * @param minPrice Минимальная цена продукта
     * @param maxPrice Максимальная цена продукта
     * @param minQuantity Минимальное количество продукта
     * @param maxQuantity Максимальное количество продукта
     * @param manufactures_list Список производителей
     * @param Rating Список рейтинга
     * @return Искомый список продуктов
     */
    List<Product> findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn(
            String filterName, int categoryId, int minPrice, int maxPrice, int minQuantity
            , int maxQuantity, List<String> manufactures_list, List<Integer> Rating);
    /**
     * Метод удаляет продукт по идентификатору
     * @param id Идентификатор продукта
     */
    Void deleteById(Long id);
}
