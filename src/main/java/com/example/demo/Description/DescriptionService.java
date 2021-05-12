package com.example.demo.Description;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class DescriptionService {
    @Autowired
    private DescriptionRepository descriptionRepository;

    public DescriptionService(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }


    public void create(Description p) {
        log.info("Save description {}", p);
        descriptionRepository.save(p);
    }

    public List<Description> readAll() {
        log.info("Find all descriptions");
        return descriptionRepository.findAll();
    }

    public Description findByProductid(int Id){
        log.info("Find description, whose Productid = {}",Id);
        return descriptionRepository.findByProductid(Id);
    }
    public Description findById(int Id){
        log.info("Find description, whose Id = {}",Id);
        return descriptionRepository.findById(Id);
    }
    public void delete(Description p){
        log.info("Delete description, whose Id = {}",p.getId());
        descriptionRepository.deleteById(p.getId());
    }
}
