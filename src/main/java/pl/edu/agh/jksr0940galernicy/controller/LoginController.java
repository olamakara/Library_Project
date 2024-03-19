package pl.edu.agh.jksr0940galernicy.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.service.AuthService;
import reactor.core.scheduler.Schedulers;

@Component
@Slf4j
public class LoginController {

    private final AuthService authService;

    private final ScreenController screenController;

    @FXML
    private TextField emailField;

    @FXML
    private Label errorLabel;

    public LoginController (@Autowired AuthService authService, @Autowired ScreenController screenController) {
        this.authService = authService;
        this.screenController = screenController;
    }

    @FXML
    private void login(ActionEvent event) {
        if (emailField.getText().isEmpty()) {
            errorLabel.setText("Wprowadź adres email.");
        } else if (!AddUserController.isValidEmail(emailField.getText())) {
            errorLabel.setText("Nieprawidłowy format emaila.");
        }
        else {
            var email = emailField.getText();
            errorLabel.setText("Ładowanie...");
            var result = authService.authenticate(email);
            if (result) {
                errorLabel.setText("Zalogowano.");
                screenController.activateScene(ScreenController.HOME_VIEW_PATH);
            } else {
                errorLabel.setText("Nie znaleziono użytkownika.");
            }
        }
    }
}
