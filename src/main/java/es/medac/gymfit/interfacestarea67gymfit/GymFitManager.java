package es.medac.gymfit.interfacestarea67gymfit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image; // ¡No olvides este import!
import javafx.stage.Stage;

import java.io.IOException;

public class GymFitManager extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GymFitManager.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 800);

        try {
            Image icono = new Image(GymFitManager.class.getResource("icono.jpg").toExternalForm());
            stage.getIcons().add(icono);
        } catch (Exception e) {
            System.out.println("No se ha encontrado el icono");
        }

        stage.setTitle("GymFit Manager");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}