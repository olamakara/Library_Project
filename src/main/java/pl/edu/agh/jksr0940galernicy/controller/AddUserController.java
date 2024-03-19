package pl.edu.agh.jksr0940galernicy.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.UserType;
import pl.edu.agh.jksr0940galernicy.service.UserService;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class AddUserController {
    @FXML
    public ComboBox<UserType> roleComboBox;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    private final UserService userService;
    private final ScreenController screenController;

    public AddUserController(@Autowired UserService userService, @Autowired ScreenController screenController) {
        this.userService = userService;
        this.screenController = screenController;
    }

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll(UserType.values());
        roleComboBox.getSelectionModel().selectFirst();
        screenController.setSceneLabel("Dodaj użytkownika");
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            errorLabel.setText("Wprowadź wszystkie dane.");
        } else if (!isValidEmail(emailField.getText())) {
            errorLabel.setText("Nieprawidłowy format emaila.");
        } else {
            log.info("Adding user..." + Thread.currentThread().getName());
            errorLabel.setText("Ładowanie.");

            userService.addUser(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(), roleComboBox.getValue()
            ).subscribeOn(Schedulers.boundedElastic())
                    .subscribe(user -> Platform.runLater(() -> {
                        errorLabel.setText("Dodano użytkownika " + user.getName());
                        firstNameField.clear();
                        lastNameField.clear();
                        emailField.clear();
                    }), error -> Platform.runLater(() -> {
                        errorLabel.setText(error.getMessage());
                        log.error(error.getMessage());
                    })
            );
        }
    }

    public static boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }
}
