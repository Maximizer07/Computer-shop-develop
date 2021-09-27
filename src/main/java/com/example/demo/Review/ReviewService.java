package com.example.demo.Review;

import com.example.demo.Description.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Сервис для работы с отзывами товаров из таблицы БД
 * @author Maximus
 */
@Service
public class ReviewService {
    /**
     * Репозиторий для работы с отзывами товаров из таблицы БД
     */
    @Autowired
    ReviewRepository reviewRepository;
    /**
     * Конструктор сервиса для работы с отзывами товаров из таблицы БД
     * @param reviewRepository Репозиторий для работы с отзывами товаров из таблицы БД
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Метод получает все отзывы товара из БД
     * @return Список всех отзывов
     */
    public List<Review> findAll(){
        return reviewRepository.findAll();
    }
    /**
     * Метод получает отзыв товара по идентификатору
     * @param Id Идентификатор отзыва
     * @return Объект отзыва товара
     */
    public Review findById(int Id){
        return reviewRepository.findById(Id);
    }
    /**
     * Метод добавляет отзыв товара в БД
     * @param review Объект отзыва товара
     */
    public void save(Review review){
        reviewRepository.save(review);
    }
    /**
     * Метод удаляет отзыв по идентификатору из БД
     * @param id Идентификатор отзыва товара
     */
    public void delete(Long id){
        reviewRepository.deleteById(id);
    }
}
