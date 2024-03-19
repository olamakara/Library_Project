package pl.edu.agh.jksr0940galernicy.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
@Slf4j
public class ScreenController {

    public static String HOME_VIEW_PATH = "view/homeView.fxml";
    public static String ADD_USER_VIEW_PATH = "view/addUserView.fxml";
    public static String ADD_BOOK_VIEW_PATH = "view/addBookView.fxml";
    public static String ADMIN_BOOK_LIST_VIEW_PATH = "view/adminBookList.fxml";
    public static String LOANS_LIST_VIEW_PATH = "view/loansList.fxml";
    public static String USER_LOANS_LIST_VIEW_PATH = "view/userLoansList.fxml";
    public static String AVAILABLE_BOOKS_LIST_VIEW_PATH = "view/availableBooksList.fxml";
    public static String NOTIFICATIONS_VIEW_PATH = "view/notificationsView.fxml";

    public static String RECOMMENDATIONS_LIST_VIEW_PATH = "view/recommendationsList.fxml";
    public static String LOANS_LIST_LAST_SIX_MONTHS_VIEW_PATH = "view/loansListLastSixMonths.fxml";

    private FXMLLoader loader;
    private Stack<String> history = new Stack<>();
    private Stage main;
    private ToolBar toolBar;
    private Label sceneLabel;
    private ApplicationContext applicationContext;

    public void configureApp(Stage stage, ApplicationContext applicationContext) {
        try {
            this.main = stage;
            this.applicationContext = applicationContext;
            loader = new FXMLLoader();
            loader.setControllerFactory(applicationContext::getBean);
            loader.setLocation(ScreenController.class.getClassLoader().getResource("view/loginView.fxml"));

            BorderPane rootLayout = loader.load();
            configureToolBar();

            configureStage(stage, rootLayout);

            stage.setWidth(900);
            stage.setHeight(850);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureToolBar() {
        toolBar = new ToolBar();
        Pane leftSpacer = new Pane();
        HBox.setHgrow(leftSpacer, Priority.SOMETIMES);

        Pane rightSpacer = new Pane();
        HBox.setHgrow(rightSpacer, Priority.SOMETIMES);

        toolBar.setMinHeight(50);

        sceneLabel = new Label("Czytelnia - Galernicy");

        Button leftArrow = new Button("<===");
        leftArrow.setOnAction(event -> {
            if (history.size() > 1) {
                history.pop();
                activateScene(history.peek());
            }
        });

        Button notificationButton = new Button("Powiadomienia");
        notificationButton.setOnAction(event -> {
            activateScene(NOTIFICATIONS_VIEW_PATH);
        });

        Button homeButton = new Button("Strona główna");
        homeButton.setOnAction(event -> {
            activateScene(HOME_VIEW_PATH);
        });

        Button logoutButton = new Button("Wyloguj");
        logoutButton.setOnAction(event -> {
            configureApp(main, applicationContext);
        });

        toolBar.getItems().addAll(leftArrow, leftSpacer , sceneLabel, rightSpacer, logoutButton, homeButton, notificationButton);

    }

    public void activateScene(String location) {
        try {
            log.info("Loading scene: " + location);
            loader = new FXMLLoader();
            loader.setControllerFactory(applicationContext::getBean);
            loader.setLocation(ScreenController.class.getClassLoader().getResource(location));
            BorderPane rootLayout = loader.load();
            rootLayout.setTop(toolBar);
            Scene scene = new Scene(rootLayout);

            main.setScene(scene);
            history.add(location);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSceneLabel(String label) {
        sceneLabel.setText(label);
    }

    private void configureStage(Stage primaryStage, BorderPane rootLayout) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Czytelnia - Galernicy");
        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }


}
