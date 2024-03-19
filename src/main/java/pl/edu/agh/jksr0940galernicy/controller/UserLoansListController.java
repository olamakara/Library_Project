package pl.edu.agh.jksr0940galernicy.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.service.AuthService;
import pl.edu.agh.jksr0940galernicy.service.LoanService;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UserLoansListController {
    private final LoanService loanService;

    private final AuthService authService;

    @FXML
    private ListView<Loan> userLoansListView;

    @FXML
    private Label topUserLoansListLabel;

    private ScreenController screenController;


    public UserLoansListController(LoanService loanService, AuthService authService, ScreenController screenController) {
        this.loanService = loanService;
        this.authService = authService;
        this.screenController = screenController;

    }

    @FXML
    private void initialize() {
        log.info("UserLoansListController initialized");
        List<Loan> userLoans = loanService.getAllLoans();

        userLoans.removeIf(loan -> !Objects.equals(authService.getLoggedUser().getId(), loan.getUser().getId()));

        userLoansListView.setItems(FXCollections.observableList(userLoans));

        userLoansListView.setCellFactory(param -> new ListCell<Loan>() {
            @Override
            protected void updateItem(Loan loan, boolean empty) {
                super.updateItem(loan, empty);

                if (empty || loan == null) {
                    setText(null);
                } else {
                    BorderPane borderPane = new BorderPane();
                    borderPane.setCenter(new Label(loan.getBook().getBookInfo().getTitle() + " - termin wypożyczenia: " + loan.getBorrowingDate() + " - termin zwrotu: " + loan.getDueDate()));
                    borderPane.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 2px");
                    setText(null);
                    setGraphic(borderPane);
                }
            }
        });

        screenController.setSceneLabel("Moje wypożyczone książki:");

    }
}
