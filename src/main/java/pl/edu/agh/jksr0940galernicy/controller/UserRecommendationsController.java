package pl.edu.agh.jksr0940galernicy.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.UI.CzytelniaApplication;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.BookCategory;
import pl.edu.agh.jksr0940galernicy.model.BookInfo;
import pl.edu.agh.jksr0940galernicy.service.AuthService;
import pl.edu.agh.jksr0940galernicy.service.BookService;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Component
@Slf4j
public class UserRecommendationsController {
    private final BookService bookService;
    private final BookViewController bookViewController;
    private final AuthService authService;

    @FXML
    private ListView<Book> recommendedBooksListView;

    public UserRecommendationsController(BookService bookService, BookViewController bookViewController, @Autowired AuthService authService) {
        this.bookService = bookService;
        this.bookViewController = bookViewController;
        this.authService = authService;
    }

    @FXML
    private void initialize() {
        List<Book> recommendedBooks = bookService.getAllBooks();
        recommendedBooks.removeIf(book -> !book.isAvailable());

        BookCategory category = authService.getLoggedUser().getFavouriteCategory();
        recommendedBooks.removeIf(book -> !book.getBookInfo().getCategory().equals(category));
        recommendedBooks.removeIf(book -> book.getLoans().stream().anyMatch(loan -> loan.getUser().equals(authService.getLoggedUser())));
        recommendedBooks.sort(Comparator.comparing(Book::getPopularity).reversed());

        Set<BookInfo> uniqueBookInfos = new LinkedHashSet<>();
        List<Book> uniqueRecommendedBooks = new ArrayList<>();

        for (Book book : recommendedBooks) {
            if (uniqueBookInfos.add(book.getBookInfo())) {
                uniqueRecommendedBooks.add(book);
            }
        }

        int limit = 3;
        if (uniqueRecommendedBooks.size() > limit) {
            uniqueRecommendedBooks = uniqueRecommendedBooks.subList(0, limit);
        }

        recommendedBooksListView.setItems(FXCollections.observableArrayList(uniqueRecommendedBooks));
        recommendedBooksListView.setCellFactory(param -> new ListCell<Book>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null || book.getBookInfo() == null) {
                    setText(null);
                } else {
                    BorderPane borderPane = new BorderPane();
                    Image image = new Image(book.getCoverImage());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(100);
                    imageView.setFitWidth(100);
                    borderPane.setLeft(imageView);
                    borderPane.setCenter(new Label(book.getBookInfo().getTitle() + " - " + book.getBookInfo().getAuthor()));
                    borderPane.setRight(new Label("Popularity: " + book.getPopularity()));
                    setText(null);
                    borderPane.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {
                            showBookView(book);
                        }
                    });
                    setGraphic(borderPane);
                }
            }
        });


    }
    private void showBookView(Book book) {
        try {
            var loader = new FXMLLoader();
            loader.setLocation(CzytelniaApplication.class.getClassLoader().getResource("view/bookView.fxml"));
            loader.setControllerFactory(aClass -> bookViewController);
            Parent rootLayout = loader.load();

            bookViewController.initialize(book);

            Stage stage = new Stage();
            stage.setTitle("Book View");
            stage.setScene(new Scene(rootLayout));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
