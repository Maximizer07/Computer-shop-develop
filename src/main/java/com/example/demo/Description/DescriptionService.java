package com.example.demo.Description;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с описаниями товаров из таблицы БД
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class DescriptionService {
    /**
     * Репозиторий для работы с описаниями товаров из таблицы БД
     */
    @Autowired
    private DescriptionRepository descriptionRepository;
    /**
     * Конструктор сервиса для работы с описаниями товаров из таблицы БД
     * @param descriptionRepository Репозиторий для работы с описаниями товаров из таблицы БД
     */
    public DescriptionService(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }

    /**
     * Метод добавляет описание товара в БД
     * @param p Объект описания товара
     */
    public void create(Description p) {
        log.info("Save description {}", p);
        descriptionRepository.save(p);
    }

    /**
     * Метод получает все описания товаров из БД
     * @return Список всех описаний товаров
     */
    public List<Description> readAll() {
        log.info("Find all descriptions");
        return descriptionRepository.findAll();
    }
    /**
     * Метод получает описание товара по идентификатору
     * @param Id Идентификатор описания товара
     * @return Искомое описание товара
     */
    public Description findById(int Id){
        log.info("Find description, whose Id = {}",Id);
        return descriptionRepository.findById(Id);
    }
    /**
     * Метод удаляет описание товара
     * @param p Объект описания товара
     */
    public void delete(Description p){
        log.info("Delete description, whose Id = {}",p.getId());
        descriptionRepository.deleteById(p.getId());
    }
}
