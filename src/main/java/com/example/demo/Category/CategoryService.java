package com.example.demo.Category;

import com.ibm.icu.text.Transliterator;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * сервис работы с категориями товаров
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class CategoryService {
    /**
     * репозиторий работы с категориями товаров
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * конструктор сервиса
     * @param categoryRepository репозиторий работы с категориями товаров
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * добавление категории
     * @param c объект категории
     */
    public void create(Category c) {
        log.info("Save category {}", c);
        var CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        String result = toLatinTrans.transliterate(c.getName());
        result = result.replaceAll("\\s+", "-").toLowerCase();
        c.setEngname(result);
        categoryRepository.save(c);
    }

    /**
     * изменение категории
     * @param c объект категории
     */
    public void change(Category c){
        categoryRepository.save(c);
    }

    /**
     * получение всех категорий
     * @return список всех категорий
     */
    public List<Category> readAll() {
        log.info("Find all categories");
        return categoryRepository.findAll();
    }

    /**
     * получение категории по имени
     * @param Name английское название категориии
     * @return категория с таким английским названием
     */
    public Category findByName(String Name){
        log.info("Find category, whose Name = {}",Name);
        return categoryRepository.findByName(Name);
    }
    /**
     * получение категории по английскому имени
     * @param Name название категориии
     * @return категория с таким названием
     */
    public Category findByEngname(String Name){
        log.info("Find category, whose Eng Name = {}",Name);
        return categoryRepository.findByEngname(Name);
    }
    /**
     * получение категории по id
     * @param Id id категории
     * @return категория с таким id
     */
    public Category findById(int Id){
        log.info("Find category, whose Id = {}",Id);
        return categoryRepository.findById(Id);
    }
    /**
     * получения списка категорий по названию и id
     * @param Id id категории
     * @param Name название категории
     * @return список категорий по id продукта и названию
     */
    public List<Category> findByIdAndName(int Id, String Name){
        log.info("Find categories, whose Id and Name = {}",Id);
        return categoryRepository.findByIdAndName(Id,Name);
    }
    /**
     * удаление категории по id
     * @param c объект категории
     */
    public void delete(Category c){
        log.info("Delete category, whose Id = {}",c.getName());
        categoryRepository.deleteById(c.getId());
    }
    /**
     * удаление категории по названию и ссылке
     * @param c объект категории
     */
    public void del(Category c){
        log.info("Delete category, whose Name and Link= {}",c.getName() + c.getLink());
        categoryRepository.deleteByNameAndAndLink(c.getName(),c.getLink());
    }
}
