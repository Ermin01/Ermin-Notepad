package erkos.notepaderkos;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class ItemDialogController {

    @FXML
    private TextField shortDeskripcija;

    @FXML
    private TextArea detalisArea;

    @FXML
    private DatePicker deadlinePicker;



    public TodoItem processResults() {
        String shortDescription = shortDeskripcija.getText().trim();
        String details = detalisArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        // Provjerava da li su uneseni kratak opis, detalji i datum
        if (shortDescription.isEmpty()) {
            showErrorAlert("Unesite sva polja.");
            return null;
        } else if (details.isEmpty()) {
            showErrorAlert("Unesite detalje.");
            return null;
        } else if (deadlineValue == null) {
            showErrorAlert("Unesite datum.");
            return null;
        }

        // Dodaje stavku ako su uneseni kratak opis, detalji i datum
        TodoItem newItem = new TodoItem(shortDescription, details, deadlineValue);
        TodoData.getInstance().addTodoItem(newItem);
        return newItem;
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
