package devin.main;

import java.io.IOException;

import devin.Devin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Devin using FXML.
 */
public class Main extends Application {

    private Devin devin = new Devin();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Devin");

            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            stage.getIcons().add(logo);
            stage.setScene(scene);
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            fxmlLoader.<MainWindow>getController().setDevin(devin);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

