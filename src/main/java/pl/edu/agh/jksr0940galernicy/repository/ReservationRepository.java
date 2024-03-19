package pl.edu.agh.jksr0940galernicy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.jksr0940galernicy.model.Reservation;
import pl.edu.agh.jksr0940galernicy.model.Review;

import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    Optional<Reservation> findReservationByBookIdAndUserId(Long bookId, Long userId);
}
