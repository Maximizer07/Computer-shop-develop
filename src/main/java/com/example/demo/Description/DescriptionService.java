package com.example.demo.Description;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * сервис работы с описантями товаров
 * @author Maximus
 */
@Service
@Slf4j
@Transactional
public class DescriptionService {
    /**
     * репозиторий работы с описаниями товаров
     */
    @Autowired
    private DescriptionRepository descriptionRepository;
    /**
     * конструктор сервиса
     * @param descriptionRepository репозиторий работы с описаниями товаров
     */
    public DescriptionService(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }

    /**
     * добавление описания
     * @param p объект описания
     */
    public void create(Description p) {
        log.info("Save description {}", p);
        descriptionRepository.save(p);
    }

    /**
     * получение всех описаний
     * @return список всех описаний
     */
    public List<Description> readAll() {
        log.info("Find all descriptions");
        return descriptionRepository.findAll();
    }
    /**
     * получение описания по id
     * @param Id id описания
     * @return объект описания
     */
    public Description findById(int Id){
        log.info("Find description, whose Id = {}",Id);
        return descriptionRepository.findById(Id);
    }
    /**
     * удаления описания
     * @param p объект описания
     */
    public void delete(Description p){
        log.info("Delete description, whose Id = {}",p.getId());
        descriptionRepository.deleteById(p.getId());
    }
}
