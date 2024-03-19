package pl.edu.agh.jksr0940galernicy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.UserType;
import pl.edu.agh.jksr0940galernicy.service.AuthService;


@Component
@Slf4j
public class HomeController {

    @FXML
    public Label topLabel;

    private final ScreenController screenController;
    private final AuthService authService;

    @FXML
    public HBox controlButtons;
    @FXML
    public VBox adminButtons;
    @FXML
    public VBox employeeButtons;
    @FXML
    public VBox userButtons;

    public HomeController(@Autowired ScreenController screenController, @Autowired AuthService authService) {
       this.screenController = screenController;
       this.authService = authService;
    }

    @FXML
    public void initialize() {
        screenController.setSceneLabel("Witaj " + authService.getLoggedUser().getName() + "! (" +authService.getLoggedUser().getUserType() + ")");
        if (authService.getLoggedUser().getUserType().equals(UserType.ADMIN)) {
            adminButtons.setVisible(true);
            employeeButtons.setVisible(true);
        } else if (authService.getLoggedUser().getUserType().equals(UserType.EMPLOYEE)) {
            employeeButtons.setVisible(true);
        } else {
            userButtons.setVisible(true);
        }
    }

    @FXML
    public void addUser() {
        log.info("Opening add user view...");
        screenController.activateScene(ScreenController.ADD_USER_VIEW_PATH);
    }

    @FXML
    public void addBook() {
        log.info("Opening add book view...");
        screenController.activateScene(ScreenController.ADD_BOOK_VIEW_PATH);
    }

    @FXML
    public  void bookList() {
        log.info("Opening book list view...");
        screenController.activateScene(ScreenController.ADMIN_BOOK_LIST_VIEW_PATH);
    }

    @FXML
    public void loansList() {
        log.info("Opening loans list view...");
        screenController.activateScene(ScreenController.LOANS_LIST_VIEW_PATH);
    }

    @FXML
    public void userLoansList() {
        log.info("Opening user loans list view...");
        screenController.activateScene(ScreenController.USER_LOANS_LIST_VIEW_PATH);
    }

    @FXML
    public void recommendedBooksList() {
        log.info("Opening recommendations list view...");
        screenController.activateScene(ScreenController.RECOMMENDATIONS_LIST_VIEW_PATH);
    }

    @FXML
    public void loansListLastSixMonths() {
        log.info("Opening loans list view...");
        screenController.activateScene(ScreenController.LOANS_LIST_LAST_SIX_MONTHS_VIEW_PATH);
    }

    public void availableBooksList() {
        log.info("Opening available books list view...");
        screenController.activateScene(ScreenController.AVAILABLE_BOOKS_LIST_VIEW_PATH);
    }

}
