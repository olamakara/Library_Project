package pl.edu.agh.jksr0940galernicy.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.UI.CzytelniaApplication;
import pl.edu.agh.jksr0940galernicy.model.BookCategory;
import pl.edu.agh.jksr0940galernicy.service.BookService;
import pl.edu.agh.jksr0940galernicy.controller.BookViewController;
import pl.edu.agh.jksr0940galernicy.model.Book;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Filter;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminBookListController {


    private enum SortType {
        POPULARITY_DESC,
        POPULARITY_ASC,
        TITLE_DESC,
        TITLE_ASC,
        RATE_DESC,
        RATE_ASC

    }

    private String sortTypeString = "";

    private final BookService bookService;
    private final BookViewController bookViewController;
    public TextField titleFilter;
    public ComboBox<String> authorFilter;
    public ComboBox<BookCategory> categoryFilter;
    public Button filterButton;
    public Button clearFilterButton;
    public ComboBox<SortType> sortType;

    private List<Book> books = new ArrayList<>();
    private int numOfPages;
    private int currentPage = 0;

    @FXML
    private Pagination pagination;

    @FXML
    private GridPane mainGrid;

    private String titleFilterString = "";
    private String authorFilterString = "";
    private BookCategory categoryFilterString = null;

    public AdminBookListController(BookService bookService, BookViewController bookViewController) {
        this.bookService = bookService;
        this.bookViewController = bookViewController;
    }

    @FXML
    private void initialize() {
        log.info("AdminBookListController initialized");
        numOfPages = (int) Math.ceil(this.bookService.getNumberOfBooks() / 6.0);
        this.pagination.setPageCount(numOfPages);

        this.pagination.setPageFactory(this::createPage);
        this.sortType.setItems(FXCollections.observableArrayList(SortType.values()));
        this.sortType.setOnAction(event -> sortBooks(this.sortType.getValue()));
        this.authorFilter.setItems(FXCollections.observableArrayList(bookService.getAllAuthors()));
        this.categoryFilter.setItems(FXCollections.observableArrayList(BookCategory.values()));

        this.books = bookService.getPaginatedBooks(currentPage, 6, this.sortTypeString, this.titleFilterString, this.authorFilterString, this.categoryFilterString);
        log.info("Books: " + this.books.size());
        updateBookList();
    }

    private Node createPage(int pageIndex) {
        log.info("Creating page " + pageIndex);
        currentPage = pageIndex;
        updateBookList();
        return new BorderPane();
    }

    private void updateBookList() {
        log.info("Updating book list");
        this.books = bookService.getPaginatedBooks(currentPage, 6, this.sortTypeString, this.titleFilterString,
                this.authorFilterString, this.categoryFilterString);

        Platform.runLater(() -> {
            mainGrid.getChildren().clear();

            int rowIndex = 0;
            int colIndex = 0;

            for (Book book : this.books) {
                VBox vbox = createBookVBox(book);
                mainGrid.add(vbox, colIndex, rowIndex);

                colIndex++;
                if (colIndex == 2) {
                    colIndex = 0;
                    rowIndex++;
                }
            }
        });
    }

    private VBox createBookVBox(Book book) {
        VBox vbox = new VBox();
        vbox.setMinWidth(300);
        vbox.getStyleClass().add("vbox-with-border");

        Image image = new Image(book.getCoverImage());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        Label titleLabel = new Label(book.getBookInfo().getTitle());
        titleLabel.setFont(new javafx.scene.text.Font("Arial", 16));

        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.getChildren().addAll(imageView, titleLabel);

        Label authorLabel = new Label(book.getBookInfo().getAuthor());
        authorLabel.setWrapText(true);
        vbox.getChildren().add(authorLabel);
        vbox.setOnMouseClicked(event -> showBookView(book));

        return vbox;
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

    public void filter(ActionEvent event) {
        this.titleFilterString = titleFilter.getText();
        this.authorFilterString = authorFilter.getSelectionModel().getSelectedItem();
        this.categoryFilterString = categoryFilter.getSelectionModel().getSelectedItem();
        numOfPages = (int) Math.ceil(this.bookService.getNumberOfBooks(this.titleFilterString, this.authorFilterString, this.categoryFilterString) / 6.0);
        this.pagination.setPageCount(numOfPages);
        updateBookList();

    }

    public void clearFilter(ActionEvent event) {
        this.titleFilterString = "";
        this.authorFilterString = "";
        this.categoryFilterString = null;
        numOfPages = (int) Math.ceil(this.bookService.getNumberOfBooks() / 6.0);
        this.pagination.setPageCount(numOfPages);
        this.titleFilterString = "";
        this.authorFilterString = "";
        this.categoryFilterString = null;
        updateBookList();
    }

    private void sortBooks(SortType sortType) {
        this.sortTypeString = String.valueOf(sortType);
        updateBookList();
    }
}
