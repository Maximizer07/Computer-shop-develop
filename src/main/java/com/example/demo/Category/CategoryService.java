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


    public void create(Category c) {
        log.info("Save category {}", c);
        var CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        String result = toLatinTrans.transliterate(c.getName());
        result = result.replaceAll("\\s+", "-").toLowerCase();
        c.setEngname(result);
        categoryRepository.save(c);
    }

    public void change(Category c){
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
    public Category findByEngname(String Name){
        log.info("Find category, whose Eng Name = {}",Name);
        return categoryRepository.findByEngname(Name);
    }
    public Category findById(int Id){
        log.info("Find category, whose Id = {}",Id);
        return categoryRepository.findById(Id);
    }
    public List<Category> findByIdAndName(int Id, String Name){
        log.info("Find categories, whose Id and Name = {}",Id);
        return categoryRepository.findByIdAndName(Id,Name);
    }
    public void delete(Category c){
        log.info("Delete category, whose Id = {}",c.getName());
        categoryRepository.deleteById(c.getId());
    }
    public void del(Category c){
        log.info("Delete category, whose Name and Link= {}",c.getName() + c.getLink());
        categoryRepository.deleteByNameAndAndLink(c.getName(),c.getLink());
    }
}
