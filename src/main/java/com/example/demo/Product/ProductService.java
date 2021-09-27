package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Review.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с продуктами из таблицы БД
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class ProductService {
    /**
     * Репозиторий для работы с продуктами из таблицы БД
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * Конструктор сервиса для работы с продуктами из таблицы БД
     * @param productRepository Репозиторий для работы с продуктами из таблицы БД
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Метод добавляет продукт в БД
     * @param p Объект продукта
     */
    public void create(Product p) {
        log.info("Save product {}", p);
        productRepository.save(p);
    }
    /**
     * Метод получает список всех продуктов из БД
     * @return Список всех продуктов
     */
    public List<Product> readAll() {
        log.info("Find all products");
        return productRepository.findAll();
    }
    /**
     * Метод получает продукт по названию
     * @param Name Название продукта
     * @return Искомый продукт
     */
    public Product findByName(String Name){
        log.info("Find product, whose Name = {}",Name);
        return productRepository.findByName(Name);
    }
    /**
     * Метод получает продукт по идентификатору
     * @param Id Идентификатор продукта
     * @return Искомый продукт
     */
    public Product findById(int Id){
        log.info("Find product, whose Id = {}",Id);
        return productRepository.findById(Id);
    }

    /**
     * Метод изменяет продукт в БД
     * @param p Объект продукта
     */
    public void change(Product p){
        productRepository.save(p);
    }
    /**
     * Метод получает список продуктов по идентификатору категории
     * @param categoryId Идентификатор категории
     * @return Искомый список продуктов
     */
    public List<Product> findById_category(int categoryId){
        log.info("Find list of products, whose Id = {}",categoryId);
        return productRepository.findByCategoryId(categoryId);
    }
    /**
     * Метод удаляет продукт из БД
     * @param p Объект продукта
     */
    public void delete(Product p){
        productRepository.deleteById(p.getId());
    }
    /**
     * Метод сохраняет продукт в БД
     * @param product Объект продукта
     */
    public void save(Product product){
        productRepository.save(product);
    }

}
