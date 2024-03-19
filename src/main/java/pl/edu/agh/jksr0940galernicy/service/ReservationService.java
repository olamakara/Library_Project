package pl.edu.agh.jksr0940galernicy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.model.Reservation;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import pl.edu.agh.jksr0940galernicy.repository.LoanRepository;
import pl.edu.agh.jksr0940galernicy.repository.ReservationRepository;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReservationService(@Autowired ReservationRepository reservationRepository,
                       @Autowired UserRepository userRepository,
                       @Autowired BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public String addReservation(LocalDate reservationDate, Long userId, Long bookId) {
        var book = bookRepository.findById(bookId);
        var user = userRepository.findById(userId);
        if (book.isPresent()) {
            if (user.isPresent() && !book.get().isAvailable()) {
                Reservation reservation = new Reservation();
                reservation.setBook(book.get());
                reservation.setUser(user.get());
                reservation.setDate(reservationDate);
                reservation.setActive(true);
                reservationRepository.save(reservation);
                Book updateBook = book.get();
                bookRepository.save(updateBook);
                return null;
            } else {
                return "The book is available for borrowing";
            }
        } else {
            return "The book does not exist";
        }
    }

}
