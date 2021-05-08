package com.example.demo.Category;

import com.ibm.icu.text.Transliterator;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public String getEngName(Category c) {
        log.info("Get Eng Name {}", c);
        String st = c.getName();
        var CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        String result = toLatinTrans.transliterate(st);
        return st.replaceAll("\\s+", "-");
    }

    public void create(Category c) {
        log.info("Save category {}", c);
        categoryRepository.save(c);
    }

    public List<Category> readAll() {
        log.info("Find all categories");
        return categoryRepository.findAll();
    }

    public Category findByName(String Name){
        log.info("Find category, whose Name = {}",Name);
        return categoryRepository.findByName(Name);
    }
    public void delete(Category c){
        log.info("Delete category, whose Name = {}",c.getName());
        categoryRepository.deleteByName(c.getName());
    }
}