package pl.edu.agh.jksr0940galernicy.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.model.Reminder;
import pl.edu.agh.jksr0940galernicy.repository.LoanRepository;
import pl.edu.agh.jksr0940galernicy.repository.ReminderRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    @Mock
    LoanRepository loanRepository;

    @Mock
    ReminderRepository reminderRepository;

    @Mock
    Loan mockLoan;

    ReminderService reminderService;


    @BeforeEach
    void setUp() {
        this.reminderService = new ReminderService(reminderRepository, loanRepository);
    }

    @Test
    void addReminder_loanPresent() {
        // given
        when(loanRepository.findById(anyLong())).thenReturn(Optional.of(mockLoan));
        // when
        reminderService.addReminder(1L, "message", LocalDate.of(2023,11,30));
        // then
        Mockito.verify(reminderRepository).save(any(Reminder.class));
    }

    @Test
    void addReminder_loanNotPresent() {
        // given
        when(loanRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        reminderService.addReminder(1L, "message", LocalDate.of(2023,11,30));
        // then
        Mockito.verify(reminderRepository, Mockito.never()).save(any(Reminder.class));
    }


}
