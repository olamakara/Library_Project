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
import pl.edu.agh.jksr0940galernicy.model.BookCategory;
import pl.edu.agh.jksr0940galernicy.service.BookService;

@Component
@Slf4j
public class AddBookController {
    @FXML
    public ComboBox<BookCategory> categoryBox;
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField coverImageField;

    @FXML
    private TextField contentsField;

    @FXML
    private Label errorLabel;

    private final BookService bookService;
    private final ScreenController screenController;

    public AddBookController(BookService bookService, @Autowired ScreenController screenController) {
        this.bookService = bookService;
        this.screenController = screenController;
    }

    @FXML
    private void initialize() {
        screenController.setSceneLabel("Dodaj książkę");
        categoryBox.getItems().addAll(BookCategory.values());
        categoryBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        if (titleField.getText().isEmpty() || authorField.getText().isEmpty() || coverImageField.getText().isEmpty() || contentsField.getText().isEmpty()) {
            errorLabel.setText("Wprowadź wszystkie dane.");
        } errorLabel.setText("Ładowanie...");
            new Thread(() -> {
                log.info("Adding book..." + Thread.currentThread().getName());

                bookService.addBook(
                        titleField.getText(),
                        authorField.getText(),
                        coverImageField.getText(),
                        contentsField.getText(),
                        categoryBox.getValue()
                );

                log.info("Book added." + Thread.currentThread().getName());

                Platform.runLater(() -> {
                    errorLabel.setText("Dodano książkę.");
                    titleField.setText("");
                    authorField.setText("");
                    coverImageField.setText("");
                    contentsField.setText("");
                });
            }).start();
    }
}
