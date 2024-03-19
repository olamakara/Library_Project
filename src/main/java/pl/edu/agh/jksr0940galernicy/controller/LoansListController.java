package pl.edu.agh.jksr0940galernicy.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.service.LoanService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class LoansListController {

    private final LoanService loanService;

    @FXML
    private ListView<Loan> loansListView;

    @FXML
    private Label topLoansListLabel;

    private ScreenController screenController;

    private enum FilterType {
        LOANED,
        RETURNED
    }
    public TextField nameFilter;
    public TextField lastNameFilter;
    public ComboBox<FilterType> stateFilter;
    public Button filterButton;
    public Button clearFilterButton;

    public LoansListController(LoanService loanService, ScreenController screenController) {
        this.loanService = loanService;
        this.screenController = screenController;
    }

    @FXML
    private void initialize() {
        log.info("LoansListController initialized");
        List<Loan> allLoans = loanService.getAllLoans();
        loansListView.setItems(FXCollections.observableList(allLoans));
        stateFilter.setItems(FXCollections.observableArrayList(FilterType.values()));

        updateCells();

        screenController.setSceneLabel("Wszystkie wypożyczone książki:");

    }

    private void updateCells() {
        loansListView.setCellFactory(param -> new ListCell<Loan>() {
            @Override
            protected void updateItem(Loan loan, boolean empty) {
                super.updateItem(loan, empty);

                if (empty || loan == null) {
                    setText(null);
                } else {
                    BorderPane borderPane = new BorderPane();
                    borderPane.setLeft(new Label( loan.getUser().getName() + " " + loan.getUser().getLastName() + " - " + loan.getBook().getBookInfo().getTitle() + " - termin zwrotu: " + loan.getDueDate()));
                    Button extendLoanButton = new Button("Przedłuż");
                    borderPane.setRight(extendLoanButton);
                    extendLoanButton.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {
                            extendBookLoan(loan);
                        }
                    });
                    if (loan.getReturnDate() != null) {
                        extendLoanButton.setDisable(true);
                    }
                    borderPane.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 2px");
                    setText(null);
                    setGraphic(borderPane);
                }
            }
        });
    }

    private void extendBookLoan(Loan loan) {
        loanService.extendBookLoan(loan.getId());
        filter();
//        loansListView.setItems(FXCollections.observableList(loanService.getAllLoans()));
    }

    public void filter() {
        String name = nameFilter.getText();
        String lastName = lastNameFilter.getText();
        Object loaned = stateFilter.getValue();
        if (Objects.equals(FilterType.LOANED, loaned)) {
            loaned = true;
        } else if (Objects.equals(FilterType.RETURNED, loaned)) {
            loaned = false;
        }
        loansListView.setItems(FXCollections.observableList(loanService.getFilteredLoans(name, lastName, loaned)));
        updateCells();
    }

    public void filter(ActionEvent event) {
        filter();
    }

    public void clearFilter(ActionEvent event) {
        nameFilter.setText("");
        lastNameFilter.setText("");
        stateFilter.setPromptText("Stan");
        stateFilter.setValue(null);
        loansListView.setItems(FXCollections.observableList(loanService.getAllLoans()));
    }

}
