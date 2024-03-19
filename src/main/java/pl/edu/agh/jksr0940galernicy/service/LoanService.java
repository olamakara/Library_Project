package pl.edu.agh.jksr0940galernicy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import pl.edu.agh.jksr0940galernicy.repository.LoanRepository;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(@Autowired LoanRepository loanRepository,
                       @Autowired UserRepository userRepository,
                       @Autowired BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public String addLoan(LocalDate borrowingDate, Long userId, Long bookId) {
        var book = bookRepository.findById(bookId);
        var user = userRepository.findById(userId);
        if (book.isPresent()) {
            if (user.isPresent() && book.get().isAvailable()) {
                Loan loan = new Loan();
                loan.setBook(book.get());
                loan.setUser(user.get());
                loan.setBorrowingDate(borrowingDate);
                loan.setDueDate(borrowingDate.plusMonths(1));
                loanRepository.save(loan);
                Book updateBook = book.get();
                updateBook.setAvailable(false);
                bookRepository.save(updateBook);
                return null;
            } else {
                return "The book is not available or the user does not exist";
            }
        } else {
            return "The book does not exist";
        }
    }

    public String returnBook(Long bookId) {
        var book = bookRepository.findById(bookId);
        var loan = book.get().getLoans().stream().filter(el -> el.getReturnDate() == null).findFirst();
        if (book.isPresent()) {
            Book updateBook = book.get();
            updateBook.setAvailable(true);
            bookRepository.save(updateBook);
            if (loan.isPresent()) {
                Loan updateLoan = loan.get();
                updateLoan.setReturnDate(LocalDate.now());
                loanRepository.save(updateLoan);
                return null;
            } else {
                return "The book is not borrowed";
            }

        } else {
            System.out.println("Book does not exist");
        }
        return null;
    }

    public List<Loan> getAllLoans() {
        return StreamSupport.stream(loanRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Loan> getFilteredLoans(String name, String lastName, Object loaned) {
        return getAllLoans().stream().filter(loan -> {
            boolean nameMatch = Objects.equals(name, "") || loan.getUser().getName().equals(name);
            boolean lastNameMatch = Objects.equals(lastName, "") || loan.getUser().getLastName().equals(lastName);
            boolean loanedMatch = loaned == null || (loaned.equals(true) && loan.getReturnDate() == null) || (loaned.equals(false) && loan.getReturnDate() != null);
            return nameMatch && lastNameMatch && loanedMatch;
        }).collect(Collectors.toList());
    }

    public void extendBookLoan(Long loanId) {
        var loan = loanRepository.findById(loanId);
        if (loan.isPresent()) {
            Loan editedLoan = loan.get();
            editedLoan.setDueDate(editedLoan.getDueDate().plusMonths(1));
            loanRepository.save(editedLoan);
        } else {
            log.info("Could not find the loan to extend.");
        }
    }

}
