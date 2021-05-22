package com.example.demo;

import com.google.common.collect.Lists;
import com.google.common.base.Functions;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

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
    public List<Product> takeProducts(String Name, Integer Id, Integer Price, Integer Quantity, Integer Idcategory) {
        Product product = new Product();
        Category category = new Category();
        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase()
                .withIgnorePaths("rating")
                .withMatcher("Id", exact())
                .withMatcher("Category", exact())
                .withMatcher("Price", exact())
                .withMatcher("Quantity", exact());
        if (Id==null) product.setId(null);
        else product.setId(Id);
        if (Price==null)  matcher = matcher.withIgnorePaths("price");
        else product.setPrice(Price);
        if (Quantity==null)  matcher = matcher.withIgnorePaths("quantity");
        else product.setQuantity(Quantity);
        if (Idcategory==null)  matcher = matcher.withIgnorePaths("category");
        else product.setCategory(categoryRepository.findById(Idcategory.intValue()));
        if (!Name.isEmpty()) product.setName(Name);
        else product.setName(null);

        Example<Product> example = Example.of(product, matcher);
        System.out.println(example);
        return productRepository.findAll(example);
    }
    public List<Category> takeCategories(String Name, Integer Id) {
        Category category = new Category();
        category.setId(Id);
        if (!Name.isEmpty()) {
            category.setName(Name);
        }
        else {
            category.setName(null);
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("Id", exact())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
        Example<Category> example = Example.of(category, matcher);
        System.out.println(example);
        return categoryRepository.findAll(example);
    }

    public List<Product> takeProductList(Category category, int[] rating, int quantity,
                                          String[] manufactures_list, String filterName,
                                          Integer minPrice, Integer maxPrice, List<String> manufactures_set) {
        int MinPrice = 0;
        int MaxPrice = 1000000;
        if (minPrice !=null){
            MinPrice = minPrice;
        }
        if (maxPrice !=null){
            MaxPrice = maxPrice;
        }
        int minQuantity = 0;
        int maxQuantity = 1000000;
        if (quantity == 2) {
            minQuantity = 1;
        }
        if (quantity == 3){
            maxQuantity = 0;
        }
        List<Integer> Rating  = Arrays.asList(0, 1, 2, 3, 4, 5);
        if (rating!=null){
            Rating = IntStream.of(rating).boxed().collect(Collectors.toList());
        }
        if (manufactures_list!=null){
            manufactures_set = Arrays.asList(manufactures_list);
        }
        return productRepository.findByNameContainingIgnoreCaseAndAndCategoryIdAndPriceBetweenAndQuantityBetweenAndManufacturerInAndRatingIn(
                filterName, category.getId(), MinPrice, MaxPrice, minQuantity, maxQuantity, manufactures_set, Rating);
    }
}