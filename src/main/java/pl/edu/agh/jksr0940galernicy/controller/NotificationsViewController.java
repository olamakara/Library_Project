package pl.edu.agh.jksr0940galernicy.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.model.Notification;
import pl.edu.agh.jksr0940galernicy.service.AuthService;
import pl.edu.agh.jksr0940galernicy.service.LoanService;
import pl.edu.agh.jksr0940galernicy.service.NotificationService;

import java.util.List;
import java.util.Objects;
@Component
@Slf4j
public class NotificationsViewController {
    private final NotificationService notificationService;

    private final AuthService authService;

    @FXML
    private ListView<Notification> notificationsListView;

    @FXML
    private Label topNotificationsListLabel;

    private ScreenController screenController;


    public NotificationsViewController(NotificationService notificationService, AuthService authService, ScreenController screenController) {
        this.notificationService = notificationService;
        this.authService = authService;
        this.screenController = screenController;
    }

    @FXML
    private void initialize() {

        List<Notification> allNotifications = notificationService.getAllNotifications();
        allNotifications = allNotifications.stream()
                .filter(notification -> Objects.equals(notification.getUser().getId(), authService.getLoggedUser().getId()))
                .toList();

        notificationsListView.setItems(FXCollections.observableList(allNotifications));

        notificationsListView.setCellFactory(param -> new ListCell<Notification>() {
            @Override
            protected void updateItem(Notification notification, boolean empty) {
                super.updateItem(notification, empty);

                if (empty || notification == null) {
                    setText(null);
                } else {
                    setText(notification.toString());
                    System.out.println(notification.toString());
                }
            }
        });

        screenController.setSceneLabel("Wszystkie powiadomienia:");

    }

    @FXML
    private void handleSendNotification() {
        Notification notification = new Notification();
        notificationService.sendNotification(notification.getId());
        notificationsListView.getItems().add(notification);
    }

    @FXML
    private void handleDeleteNotification() {
        Notification notification = notificationsListView.getSelectionModel().getSelectedItem();
        notificationService.deleteNotification(notification.getId());
        notificationsListView.getItems().remove(notification);
    }


}
