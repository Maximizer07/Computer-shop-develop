package com.example.demo;

import com.example.demo.Review.Review;
import com.example.demo.Review.ReviewRepository;
import com.example.demo.Review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {
    @Mock
    private ReviewRepository reviewRepository;
    @Captor
    ArgumentCaptor<Review> captor;

    @Test
    void getReviews() {
        Review review1 = new Review();
        review1.setId(1L);
        review1.setComment("Test1");
        Review review2 = new Review();
        review2.setId(1L);
        review2.setComment("Test2");
        ReviewService rs =new ReviewService(reviewRepository);
        Mockito.when(reviewRepository.findAll()).thenReturn(List.of(review1,
                review2));
        assertEquals(2,
                rs.findAll().size());
        assertEquals("Test2",
                reviewRepository.findAll().get(0).getComment());
    }
    @Test
    void ReviewFindById() {
        Review review1 = new Review();
        review1.setId(1L);
        review1.setComment("Test1");
        ReviewService rs =new ReviewService(reviewRepository);
        Mockito.when(reviewRepository.findById(1)).thenReturn(review1);
        assertEquals("Test1",
                rs.findById(1).getComment());
    }

    @Test
    void ReviewDelete() {
        Review review1 = new Review();
        review1.setId(1L);
        review1.setComment("Test1");
        ReviewService rs =new ReviewService(reviewRepository);
        Mockito.when(reviewRepository.findById(1)).thenReturn(review1);
        assertEquals("Test1",
                rs.findById(1).getComment());
        rs.delete(review1.getId());
        Mockito.verify(reviewRepository).deleteById(1L);
        assertEquals(0,rs.findAll().size());
    }
}
