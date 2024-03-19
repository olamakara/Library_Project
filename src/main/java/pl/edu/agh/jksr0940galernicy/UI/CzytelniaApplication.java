package pl.edu.agh.jksr0940galernicy.UI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.edu.agh.jksr0940galernicy.JkSr0940GalernicyApplication;
import pl.edu.agh.jksr0940galernicy.commons.DataFeeder;
import pl.edu.agh.jksr0940galernicy.controller.ScreenController;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class CzytelniaApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    private ScreenController screenController;

    @Override
    public void init() {
        log.info("Starting application...");
        applicationContext = new SpringApplicationBuilder(JkSr0940GalernicyApplication.class).run();
        screenController = applicationContext.getBean(ScreenController.class);
        DataFeeder dataFeeder = applicationContext.getBean(DataFeeder.class);
        //dataFeeder.feed();

    }

    @Override
    public void start(Stage stage) {
        screenController.configureApp(stage, applicationContext);
    }



    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
}