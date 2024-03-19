package pl.edu.agh.jksr0940galernicy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.repository.BookRepository;
import pl.edu.agh.jksr0940galernicy.repository.LoanRepository;
import pl.edu.agh.jksr0940galernicy.repository.UserRepository;
import pl.edu.agh.jksr0940galernicy.model.Book;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    LoanRepository loanRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    Book mockBook;
    @Mock
    User mockUser;

    LoanService loanService;


    @BeforeEach
    void setUp() {
        this.loanService = new LoanService(loanRepository, userRepository, bookRepository);
    }

    @Test
    void addLoan_userPresent_bookPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mockBook));
        // when
        loanService.addLoan(LocalDate.of(2023,11,30), 1L, 1L);
        // then
        Mockito.verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void addLoan_userNotPresent_bookPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(mockBook));
        // when
        loanService.addLoan(LocalDate.of(2023,11,30), 1L, 1L);
        // then
        Mockito.verify(loanRepository, Mockito.never()).save(any(Loan.class));
    }

    @Test
    void addLoan_userPresent_bookNotPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        loanService.addLoan(LocalDate.of(2023,11,30), 1L, 1L);
        // then
        Mockito.verify(loanRepository, Mockito.never()).save(any(Loan.class));
    }

    @Test
    void addLoan_userNotPresent_bookNotPresent() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        loanService.addLoan(LocalDate.of(2023,11,30), 1L, 1L);
        // then
        Mockito.verify(loanRepository, Mockito.never()).save(any(Loan.class));
    }
}
