package pl.edu.agh.jksr0940galernicy.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.Book;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.model.Reservation;
import pl.edu.agh.jksr0940galernicy.model.User;
import pl.edu.agh.jksr0940galernicy.service.*;

import javafx.scene.image.ImageView;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BookViewController {

    @FXML
    public Text errorText;
    public GridPane leaveOpinion;
    public ComboBox<Integer> rating;
    public TextField comment;
    public Label yourrating;
    public Label yourcoment;
    public GridPane yourOpinion;
    public TextField ratingField;
    private Book book;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final NotificationService notificationService;
    private final AuthService authService;
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField coverImageField;

    @FXML
    private TextField contentsField;

    @FXML
    private ImageView coverImage;

    @FXML
    private LineChart<String, Number> loansChart;

    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

    private final ReviewService reviewService;

    public BookViewController(LoanService loanService, AuthService authService, ReviewService reviewService, ReservationService reservationService, NotificationService notificationService) {
        this.loanService = loanService;
        this.authService = authService;
        this.reviewService = reviewService;
        this.reservationService = reservationService;
        this.notificationService = notificationService;
    }

    public void initialize(Book book) {
        this.book = book;
        Image image = new Image(book.getCoverImage());
        coverImage.setImage(image);
        titleField.setText(book.getBookInfo().getTitle());
        authorField.setText(book.getBookInfo().getAuthor());
        coverImageField.setText(book.getCoverImage());
        contentsField.setText(book.getContents());

        rating.getItems().addAll(1, 2, 3, 4, 5);

        ratingField.setText(String.valueOf(book.getRate()));

        if(reviewService.getReviewByBookIdAndUserId(book.getId(), authService.getLoggedUser().getId()) != null){
            yourOpinion.setVisible(true);
            leaveOpinion.setVisible(false);
            yourrating.setText("Twoja ocena " + reviewService.getReviewByBookIdAndUserId(book.getId(), authService.getLoggedUser().getId()).getRate());
            yourcoment.setText("Twój komentarz: " + reviewService.getReviewByBookIdAndUserId(book.getId(), authService.getLoggedUser().getId()).getText());
        } else {
            yourOpinion.setVisible(false);
            leaveOpinion.setVisible(true);
        }
        Map<String, Integer> monthlyLoans = calculateMonthlyLoans();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        monthlyLoans.forEach((month, count) -> series.getData().add(new XYChart.Data<>(month, count)));
        loansChart.getData().add(series);
    }

    private Map<String, Integer> calculateMonthlyLoans() {
        List<Loan> loans = this.book.getLoans();

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        List<Loan> lastSixMonthsLoans = loans.stream()
                .filter(loan -> loan.getBorrowingDate().isAfter(sixMonthsAgo))
                .toList();

        Map<String, Integer> monthlyLoans = new HashMap<>();
        for (Loan loan : lastSixMonthsLoans) {
            LocalDate borrowingDate = loan.getBorrowingDate();
            String monthKey = borrowingDate.format(monthFormatter);

            monthlyLoans.put(monthKey, monthlyLoans.getOrDefault(monthKey, 0) + 1);
        }

        return monthlyLoans;
    }


    @FXML
    private void handleBorrowBook(ActionEvent event) {
        log.info("Borrowing book...");
        LocalDate date = LocalDate.now();
        String result = loanService.addLoan(date, authService.getLoggedUser().getId(), book.getId());
        if (result != null) {
            log.info(result);
            Platform.runLater(() -> errorText.setText(result));

        } else {
            log.info("Book borrowed");
            closeWindow();
        }
    }
    @FXML
    private void handleReserveBook(ActionEvent event) {
        log.info("Reserving book...");
        LocalDate date = LocalDate.now();
        String result = reservationService.addReservation(date, authService.getLoggedUser().getId(), book.getId());
        if (result != null) {
            log.info(result);
            Platform.runLater(() -> errorText.setText(result));

        } else {
            log.info("Book reserved");
            closeWindow();
        }
    }

    @FXML
    private void handleReturnBook(ActionEvent event) {
        log.info("Returning book...");
        String result = loanService.returnBook(book.getId());
        String result2 = notificationService.sendNotification(book.getId());
        if (result != null) {
            log.info(result);
            Platform.runLater(() -> errorText.setText(result));

        } else if (result2 != null) {
            log.info(result2);
            Platform.runLater(() -> errorText.setText(result2));
        } else {
            log.info("Book returned");
            closeWindow();
        }

    }

    private void closeWindow() {
        ((Stage) titleField.getScene().getWindow()).close();
    }

    public void handleLeaveOpinion(ActionEvent event) {
        log.info("Leaving opinion...");
        String result = reviewService.addReview(authService.getLoggedUser().getId(), book.getId(), comment.getText(), rating.getValue());
        if (result != null) {
            log.info(result);
             Platform.runLater(() -> errorText.setText(result));

        } else {
            log.info("Opinion left");
            leaveOpinion.setVisible(false);
            yourOpinion.setVisible(true);
            yourrating.setText("Twoja ocena: " + rating.getValue());
            yourcoment.setText("Twój komentarz: " + comment.getText());
        }
    }
}
