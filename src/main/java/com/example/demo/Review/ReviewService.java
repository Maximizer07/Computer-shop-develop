package com.example.demo.Review;

import com.example.demo.Description.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * сервис работы с отзывами товаров
 * @author Maximus
 */
@Service
public class ReviewService {
    /**
     * репозиторий работы с отзывами товаров
     */
    @Autowired
    ReviewRepository reviewRepository;
    /**
     * конструктор сервиса
     * @param reviewRepository репозиторий работы с отзывами товаров
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * получение всех отзывов
     * @return список всех отзывов
     */
    public List<Review> findAll(){
        return reviewRepository.findAll();
    }
    /**
     * получение отзыва по id
     * @param Id id отзыва
     * @return объект отзыва
     */
    public Review findById(int Id){
        return reviewRepository.findById(Id);
    }
    /**
     * добавление отзыва
     * @param review объект отзыва
     */
    public void save(Review review){
        reviewRepository.save(review);
    }
    /**
     * удаления отзыва по id
     * @param id id отзыва
     */
    public void delete(Long id){
        reviewRepository.deleteById(id);
    }
}
