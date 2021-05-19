package com.example.demo;

import com.example.demo.Category.Category;
import com.example.demo.Category.CategoryRepository;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CriteriaService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    private final SessionFactory sessionFactory;
    private Session session;
    @PostConstruct
    void init() {
        session = sessionFactory.openSession();
    }
    @PreDestroy
    void closeSession() {
        session.close();
    }
    public List<Product> takeProducts(String Name, String Id, String Price, String Quantity) {
        Product product = new Product();
        ExampleMatcher matcher;
        if (!Id.isEmpty()) {
            product.setId(Integer.parseInt(Id));
        }
        else {
            product.setId(null);
        }
        if (!Name.isEmpty()) {
            product.setName(Name);
        }
        else {
            product.setName(null);
        }
        if (!Price.isEmpty()&!Quantity.isEmpty()){
            product.setPrice(Integer.parseInt(Price));
            product.setQuantity(Integer.parseInt(Quantity));
            matcher = ExampleMatcher.matching()
                    .withIgnorePaths("rating")
                    .withMatcher("Id", startsWith())
                    .withMatcher("Name", exact())
                    .withMatcher("Price", exact())
                    .withMatcher("Quantity", exact());
        }
        else if (!Price.isEmpty() & Quantity.isEmpty()) {
            product.setPrice(Integer.parseInt(Price));
            matcher = ExampleMatcher.matching()
                    .withIgnorePaths("rating")
                    .withIgnorePaths("quantity")
                    .withMatcher("Id", startsWith())
                    .withMatcher("Name", exact())
                    .withMatcher("Price", exact());
        }
        else if (!Quantity.isEmpty() & Price.isEmpty()) {
            product.setQuantity(Integer.parseInt(Quantity));
            matcher = ExampleMatcher.matching()
                    .withIgnorePaths("rating")
                    .withIgnorePaths("price")
                    .withMatcher("Id", startsWith())
                    .withMatcher("Name", exact())
                    .withMatcher("Quantity", exact());
        }
        else {
            matcher = ExampleMatcher.matching()
                    .withIgnorePaths("rating")
                    .withIgnorePaths("price")
                    .withIgnorePaths("quantity")
                    .withMatcher("Id", startsWith())
                    .withMatcher("Name", exact());
        }
        Example<Product> example = Example.of(product, matcher);
        System.out.println(example);
        return productRepository.findAll(example);
    }
    public List<Category> takeCategories(String Name, String Id) {
        Category category = new Category();
        if (!Id.isEmpty() & !Name.isEmpty()){
            category.setId(Integer.parseInt(Id));
            category.setName(Name);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("Id", startsWith())
                    .withMatcher("Name", exact());
            Example<Category> example = Example.of(category, matcher);
            return categoryRepository.findAll(example);
        }
        else if (!Id.isEmpty()){
            category.setId(Integer.parseInt(Id));
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("Id", startsWith());
            Example<Category> example = Example.of(category, matcher);
            return categoryRepository.findAll(example);
        }
        else if (!Name.isEmpty()){
            category.setName(Name);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("Name", startsWith());
            Example<Category> example = Example.of(category, matcher);
            return categoryRepository.findAll(example);
        }
        return categoryRepository.findAll();
    }
}