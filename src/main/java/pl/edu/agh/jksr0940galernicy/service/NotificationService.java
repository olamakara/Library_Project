package pl.edu.agh.jksr0940galernicy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.jksr0940galernicy.repository.NotificationRepository;
import pl.edu.agh.jksr0940galernicy.repository.ReservationRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import pl.edu.agh.jksr0940galernicy.model.Notification;

@Slf4j
@Service
public class NotificationService {
    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public NotificationService(@Autowired ReservationRepository reservationRepository, @Autowired NotificationRepository notificationRepository, AuthService authService) {
        this.reservationRepository = reservationRepository;
        this.notificationRepository = notificationRepository;
        this.authService = authService;

    }

    public String sendNotification(Long bookId) {
        var userId = authService.getLoggedUser().getId();
        var reservation = reservationRepository.findReservationByBookIdAndUserId(bookId, userId);
        if (reservation.isPresent()) {
            Notification notification = new Notification();
            notification.setUser(reservation.get().getUser());
            notification.setBook(reservation.get().getBook());
            String message = "Książka  jest dostępna. Możesz ją odebrać w bibliotece.";
            System.out.println(message);
            notification.setMessage(message);
            notification.setDate(java.time.LocalDate.now());
            reservationRepository.delete(reservation.get());
            notificationRepository.save(notification);
        } else {
            System.out.println("Reservation does not exist");
        }

        return null;
    }

    public void deleteNotification(Long notificationId) {
        var notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            notificationRepository.delete(notification.get());
        } else {
            System.out.println("Notification does not exist");
        }
    }

    public List<Notification> getAllNotifications() {
        return StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }




}
