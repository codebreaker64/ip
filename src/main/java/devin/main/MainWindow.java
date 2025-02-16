package devin.main;

import java.io.IOException;

import devin.Devin;
import devin.dialogbox.DialogBox;
import devin.exception.DevinException;
import devin.ui.Ui;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Devin devin;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.jpg"));
    private Image devinImage = new Image(this.getClass().getResourceAsStream("/images/DaDevin.jpg"));

    /**
     * Methods to start when the app first initialise
     */
    @FXML
    public void initialize() {
        try {
            scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
            Devin.main(null);
            dialogContainer.getChildren().add(DialogBox.getDevinDialog(Ui.printGreet(), devinImage));
            userInput.setPromptText("Type here...");
        } catch (DevinException | IOException e) {
            handleException(e.getMessage());
        }
    }

    /** Injects the devin instance */
    public void setDevin(Devin d) {
        devin = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing devin's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        try {
            String input = userInput.getText();
            assert input != null: "There is no user input.";
            String response = devin.getResponse(input);
            assert response != null: "There is no response.";
            dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDevinDialog(response, devinImage)
            );
            userInput.clear();
            //Solution inspired by ypuppy at https://github.com/nus-cs2103-AY2425S2/forum/issues/160
            if (input.contains("bye")) {
                PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
                delay.setOnFinished(event -> Platform.exit());
                delay.play();

            }
        } catch (DevinException | IOException e) {
            handleException(e.getMessage());
            userInput.clear();
        }
    }
    private void handleException(String error) {
        dialogContainer.getChildren().add(DialogBox.getDevinDialog(error, devinImage));
    }
}
