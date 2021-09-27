package com.example.demo.Category;

import com.ibm.icu.text.Transliterator;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с категориями товаров из таблицы БД
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class CategoryService {
    /**
     * Репозиторий для работы с категориями товаров из таблицы БД
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Конструктор сервиса для работы с категориями товаров из таблицы БД
     * @param categoryRepository Репозиторий для работы с категориями товаров из таблицы БД
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Метод добавляет категорию товаров в БД
     * @param c Объект категории товаров
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
     * Метод измененяет категорию товаров в БД
     * @param c Объект категории товаров
     */
    public void change(Category c){
        categoryRepository.save(c);
    }

    /**
     * Метод получает все категории товаров из БД
     * @return Список всех категорий товаров
     */
    public List<Category> readAll() {
        log.info("Find all categories");
        return categoryRepository.findAll();
    }

    /**
     * Метод получает категорию товаров по имени
     * @param Name Название категориии
     * @return Искомая категория
     */
    public Category findByName(String Name){
        log.info("Find category, whose Name = {}",Name);
        return categoryRepository.findByName(Name);
    }
    /**
     * Метод получает категорию по английскому имени
     * @param Name Английское название категориии
     * @return Искомая категория
     */
    public Category findByEngname(String Name){
        log.info("Find category, whose Eng Name = {}",Name);
        return categoryRepository.findByEngname(Name);
    }
    /**
     * Метод получает категорию по идентификатору
     * @param Id Идентификатор категории
     * @return Искомая категория
     */
    public Category findById(int Id){
        log.info("Find category, whose Id = {}",Id);
        return categoryRepository.findById(Id);
    }
    /**
     * Метод удаляет категорию по идентификатору
     * @param c Объект категории товаров
     */
    public void delete(Category c){
        log.info("Delete category, whose Id = {}",c.getName());
        categoryRepository.deleteById(c.getId());
    }
    /**
     * Метод удаляет категорию по названию и ссылке
     * @param c Объект категории товаров
     */
    public void del(Category c){
        log.info("Delete category, whose Name and Link= {}",c.getName() + c.getLink());
        categoryRepository.deleteByNameAndAndLink(c.getName(),c.getLink());
    }
}
