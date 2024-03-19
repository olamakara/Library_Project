package pl.edu.agh.jksr0940galernicy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.agh.jksr0940galernicy.model.Notification;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

}
