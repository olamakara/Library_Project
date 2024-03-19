package pl.edu.agh.jksr0940galernicy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;

import pl.edu.agh.jksr0940galernicy.UI.CzytelniaApplication;


@SpringBootApplication
public class JkSr0940GalernicyApplication {

    public static void main(String[] args) {
        //SpringApplication.run(JkSr0940GalernicyApplication.class, args);
        Application.launch(CzytelniaApplication.class, args);
    }





}
