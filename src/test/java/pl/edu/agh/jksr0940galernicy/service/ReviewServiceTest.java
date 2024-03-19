package pl.edu.agh.jksr0940galernicy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.jksr0940galernicy.model.Review;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import pl.edu.agh.jksr0940galernicy.repository.ReviewRepository;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;
import pl.edu.agh.jksr0940galernicy.model.Book;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    Book mockBook;
    @Mock
    User mockUser;

    ReviewService reviewService;


    @BeforeEach
    void setUp() {
        this.reviewService = new ReviewService(reviewRepository, userRepository, bookRepository);
    }

    @Test
    void addReview_userPresent_bookPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mockBook));
        // when
        reviewService.addReview(1L, 1L, "review", 10);
        // then
        Mockito.verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void addReview_userNotPresent_bookPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mockBook));
        // when
        reviewService.addReview(1L, 1L, "review", 5);
        // then
        Mockito.verify(reviewRepository, Mockito.never()).save(any(Review.class));
    }

    @Test
    void addReview_userPresent_bookNotPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        reviewService.addReview(1L, 1L, "review", 5);
        // then
        Mockito.verify(reviewRepository, Mockito.never()).save(any(Review.class));
    }

    @Test
    void addReview_userNotPresent_bookNotPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        reviewService.addReview(1L, 1L, "review", 6);
        // then
        Mockito.verify(reviewRepository, Mockito.never()).save(any(Review.class));
    }
}
