package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Review.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * сервис работы с продуктами
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class ProductService {
    /**
     * репозиторий работы с продуктами
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * конструктор сервиса
     * @param productRepository репозиторий работы с продуктами
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * добавление продукта
     * @param p объект продукта
     */
    public void create(Product p) {
        log.info("Save product {}", p);
        productRepository.save(p);
    }
    /**
     * получение всех продуктов
     * @return список всех продуктов
     */
    public List<Product> readAll() {
        log.info("Find all products");
        return productRepository.findAll();
    }
    /**
     * получение продукта по названию
     * @param Name название продукта
     * @return продукт
     */
    public Product findByName(String Name){
        log.info("Find product, whose Name = {}",Name);
        return productRepository.findByName(Name);
    }
    /**
     * получение продукта по id
     * @param Id id продукта
     * @return объект продукта
     */
    public Product findById(int Id){
        log.info("Find product, whose Id = {}",Id);
        return productRepository.findById(Id);
    }
    /**
     * получение продукта по id
     * @param number id продукта
     * @return объект продукта
     */
    public Product findBynumber(int number){
        return productRepository.findById(number);
    }
    /**
     * изменение продукта
     * @param p объект продукта
     */
    public void change(Product p){
        productRepository.save(p);
    }
    /**
     * получение списка продуктов по id категории
     * @param categoryId id категории
     * @return список продуктов
     */
    public List<Product> findById_category(int categoryId){
        log.info("Find list of products, whose Id = {}",categoryId);
        return productRepository.findByCategoryId(categoryId);
    }
    /**
     * удаления продукта
     * @param p объект продукта
     */
    public void delete(Product p){
        productRepository.deleteById(p.getId());
    }
    /**
     * сохранение продукта
     * @param product объект продукта
     */
    public void save(Product product){
        productRepository.save(product);
    }

}
