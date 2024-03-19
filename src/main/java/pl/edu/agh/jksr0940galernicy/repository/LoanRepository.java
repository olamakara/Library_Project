package pl.edu.agh.jksr0940galernicy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.jksr0940galernicy.model.Loan;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {

    Optional<Loan> findLoanByDueDateAfter(LocalDate dueDate);

}
