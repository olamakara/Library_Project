package pl.edu.agh.jksr0940galernicy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.Review;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import pl.edu.agh.jksr0940galernicy.repository.ReviewRepository;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;

@Slf4j
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewService(@Autowired ReviewRepository reviewRepository,
                         @Autowired UserRepository userRepository,
                         @Autowired BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public String addReview(Long userId, Long bookId, String text, int rating) {
        var user = userRepository.findById(userId);
        var book = bookRepository.findById(bookId);
        if(rating < 1 || rating > 5) return ("Rating must be between 1 and 5");

        if (user.isPresent()) {
            if (book.isPresent()) {
                Review review = new Review();
                review.setBook(book.get());
                review.setUser(user.get());
                review.setText(text);
                review.setRate(rating);
                reviewRepository.save(review);
            } else {
                return ("Book does not exist");
            }
        } else {
            return ("User does not exist");
        }
        return null;
    }

    public Review getReviewByBookIdAndUserId(Long bookId, Long userId){
        return reviewRepository.findReviewByBookIdAndUserId(bookId, userId).orElse(null);
    }
}
