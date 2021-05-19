package com.example.demo.Product;

import com.example.demo.Category.Category;
import com.example.demo.Review.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void create(Product p) {
        log.info("Save product {}", p);
        productRepository.save(p);
    }

    public List<Product> readAll() {
        log.info("Find all products");
        return productRepository.findAll();
    }
    public Product findByName(String Name){
        log.info("Find product, whose Name = {}",Name);
        return productRepository.findByName(Name);
    }
    public Product findById(int Id){
        log.info("Find product, whose Id = {}",Id);
        return productRepository.findById(Id);
    }
    public Product findBynumber(int number){
        return productRepository.findById(number);
    }
    public void change(Product p){
        productRepository.save(p);
    }
    public List<Product> findById_category(int categoryId){
        log.info("Find list of products, whose Id = {}",categoryId);
        return productRepository.findByCategoryId(categoryId);
    }
    public void delete(Product p){
        log.info("Delete product, whose Id = {}",p.getId());
        productRepository.deleteById(p.getId());
    }

    public void save(Product product){
        productRepository.save(product);
    }

}
