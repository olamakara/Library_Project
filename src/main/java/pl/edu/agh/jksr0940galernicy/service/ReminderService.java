package pl.edu.agh.jksr0940galernicy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.model.Reminder;
import pl.edu.agh.jksr0940galernicy.repository.LoanRepository;
import pl.edu.agh.jksr0940galernicy.repository.ReminderRepository;

import java.time.LocalDate;

@Slf4j
@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final LoanRepository loanRepository;

    public ReminderService(@Autowired ReminderRepository reminderRepository,
                           @Autowired LoanRepository loanRepository) {
        this.reminderRepository = reminderRepository;
        this.loanRepository = loanRepository;
    }

    public void addReminder(Long loanId, String message, LocalDate date) {
        var loan = loanRepository.findById(loanId);
        if (loan.isPresent()) {
            Reminder reminder = new Reminder();
            reminder.setDate(date);
            reminder.setLoan(loan.get());
            reminder.setMessage(message);
            reminderRepository.save(reminder);
        } else {
            System.out.println("Loan does not exist");
        }
    }
}
