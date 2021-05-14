package com.example.demo.Review;

import com.example.demo.WishItem.WishItem;
import com.example.demo.WishItem.WishItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewService {
    final
    ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }
    public void save(Review review){
        reviewRepository.save(review);
    }
    public void delete(Long id){
        reviewRepository.deleteById(id);
    }
}
