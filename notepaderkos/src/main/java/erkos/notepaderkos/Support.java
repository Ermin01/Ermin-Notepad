package erkos.notepaderkos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Support {

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Label characterCountLabel;

    public void initialize() {
        sendButton.setDisable(true);

        messageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int brojac = 80 - newValue.length();

            characterCountLabel.setText(brojac + " characters remaining");

            sendButton.setDisable(newValue.isEmpty());
        });
    }

    @FXML
    public void support() {
        System.out.println("heje");
    }
}
