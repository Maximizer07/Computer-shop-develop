package com.example.demo.Product;

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

    public List<Product> findById_category(int categoryId){
        log.info("Find list of products, whose Id = {}",categoryId);
        return productRepository.findByCategoryId(categoryId);
    }
    public void delete(Product p){
        log.info("Delete product, whose Id = {}",p.getId());
        productRepository.deleteById(p.getId());
    }
}