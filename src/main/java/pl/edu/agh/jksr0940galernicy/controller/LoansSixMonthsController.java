package pl.edu.agh.jksr0940galernicy.controller;


import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.agh.jksr0940galernicy.model.Loan;
import pl.edu.agh.jksr0940galernicy.service.LoanService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class LoansSixMonthsController {
    @FXML
    private LineChart<String, Number> loansChart;

    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

    private final LoanService loanService;

    public LoansSixMonthsController(LoanService loanService) {
        this.loanService = loanService;
    }

    @FXML
    public void initialize() {
        Map<String, Integer> monthlyLoans = calculateMonthlyLoans();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        monthlyLoans.forEach((month, count) -> series.getData().add(new XYChart.Data<>(month, count)));
        loansChart.getData().add(series);
    }

    private Map<String, Integer> calculateMonthlyLoans() {
        List<Loan> loans = loanService.getAllLoans();

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

}
