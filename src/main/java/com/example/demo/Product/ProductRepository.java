package com.example.demo.Product;

import com.example.demo.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
/**
 * репозиторий работы с продуктами
 * @author Maximus
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    /**
     * получение продукта по названию
     * @param name название продукта
     * @return объект продукта
     */
    Product findByName(String name);
    /**
     * получение продукта по id
     * @param id id продукта
     * @return объект продукта
     */
    Product findById(int id);
    /**
     * получение списка продуктов по id категории
     * @param categoryId id категории
     * @return список продуктов
     */
    List<Product> findByCategoryId(int categoryId);
    /**
     * поиск списка продуктов по фильтру
     * @param filterName фрагмент названия
     * @param categoryId id категории
     * @param minPrice минимальная цена продукта
     * @param maxPrice максимальная цена продукта
     * @param minQuantity минимальное количество продукта
     * @param maxQuantity максимальное количество продукта
     * @param manufactures_list список производителей
     * @param Rating список рейтинга
     * @return список продуктов
     */
    List<Product> findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn(
            String filterName, int categoryId, int minPrice, int maxPrice, int minQuantity
            , int maxQuantity, List<String> manufactures_list, List<Integer> Rating);
    /**
     * удаление продукта по id
     * @param id id продукта
     */
    Void deleteById(Long id);
}
