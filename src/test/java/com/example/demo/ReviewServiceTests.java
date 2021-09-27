package com.example.demo;

import com.example.demo.Review.Review;
import com.example.demo.Review.ReviewRepository;
import com.example.demo.Review.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {
    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Captor
    ArgumentCaptor<Review> captor;

    private Review review1, review2;
    @BeforeEach
    void init() {
        review1 = new Review();
        review1.setId(1L);
        review1.setComment("Test1");
        review2 = new Review();
        review2.setId(1L);
        review2.setComment("Test2");
    }

    @Test
    void getReviews() {
        Mockito.when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));
        assertEquals(List.of(review1, review2), reviewRepository.findAll());
    }
    @Test
    void addReview() {
        reviewService.save(review1);
        Mockito.verify(reviewRepository).save(captor.capture());
        Review captured = captor.getValue();
        assertEquals(review1, captured);
    }
    @Test
    void ReviewFindById() {
        Mockito.when(reviewRepository.findById(1)).thenReturn(review1);
        assertEquals(review1, reviewRepository.findById(1));
    }

    @Test
    void ReviewDelete() {
        reviewService.delete(2L);
        Mockito.verify(reviewRepository).deleteById(2L);
    }
}
