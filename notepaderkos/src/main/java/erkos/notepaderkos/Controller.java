package erkos.notepaderkos;

import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private List<TodoItem> todoItems;

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainmBorderPain;

    @FXML
    private ContextMenu listContextMeni;

    @FXML
    private ToggleButton filterToggleButton;

    private FilteredList<TodoItem> filteredList;

    private Predicate<TodoItem> wantAllitems;
    private Predicate<TodoItem> wantToday;




    //novo
    @FXML
    public void initialize() {
        listContextMeni = new ContextMenu();
        MenuItem deleteMenu = new MenuItem("Delete");
        deleteMenu.setOnAction(event -> {
            TodoItem item = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
        });

        listContextMeni.getItems().add(deleteMenu);

        todoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                // Provjerite da li item nije null prije nego što pokušate pristupiti detaljima i rokovima
                if (item != null) {
                    itemDetailsTextArea.setText(item.getDetalji());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // "d M yy");
                    deadlineLabel.setText(df.format(item.getDeadLine()));
                }
            }
        });

        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(), wantAllitems);

        SortedList<TodoItem> sortedList = new SortedList<>(filteredList,
                Comparator.comparing(TodoItem::getDeadLine));

        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(todoItemListView -> {
            ListCell<TodoItem> cell = new ListCell<>() {

                @Override
                protected void updateItem(TodoItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getShortDeskripcija());
                        if (item.getDeadLine().isBefore(LocalDate.now().plusDays(1))) {
                            setTextFill(Color.BLACK);
                        } else if (item.getDeadLine().equals(LocalDate.now().plusDays(1))) {
                            setTextFill(Color.BLACK);
                        }
                    }
                }
            };
            cell.emptyProperty().addListener(
                    (obs, wasEmpty, isNowEmpty) -> {
                        if (isNowEmpty) {
                            cell.setContextMenu(null);
                        } else {
                            cell.setContextMenu(listContextMeni);
                        }
                    });
            return cell;
        });
    }

@FXML
public void showNewItemDialog() {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.initOwner(mainmBorderPain.getScene().getWindow());
    dialog.setTitle("Add New Todo Item");
    dialog.setHeaderText("Use this dialog to create a new todo item");
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
    try {
        dialog.initOwner(mainmBorderPain.getScene().getWindow());
        dialog.getDialogPane().setContent(fxmlLoader.load());
    } catch (IOException e) {
        System.out.println("Couldn't load the dialog");
        e.printStackTrace();
        return;
    }

    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
    dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

    Optional<ButtonType> result = dialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        ItemDialogController controller = fxmlLoader.getController();
        TodoItem newItem = controller.processResults();
        todoListView.getSelectionModel().select(newItem);
    }
}




    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        TodoItem selecteditem = todoListView.getSelectionModel().getSelectedItem();
        if (selecteditem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selecteditem);
            }
        }
    }


@FXML
public void handleClickListView() {
    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
    itemDetailsTextArea.setText(item.getDetalji());
    deadlineLabel.setText(item.getDeadLine().toString());
}


public void deleteItem(TodoItem item) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete Todo Item");
    alert.setHeaderText("Delete item: " + item.getShortDeskripcija());
    alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out.");
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
        TodoData.getInstance().deleteItem(item);
    }
}



    @FXML
    public void handleFilterButton() {
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()) {
            filteredList.setPredicate(wantToday);
            if (filteredList.isEmpty()) {
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }
        } else {
            filteredList.setPredicate(wantAllitems);
            todoListView.getSelectionModel().select(selectedItem);
        }
    }

    @FXML
    public void handexit() {
        Platform.exit();
    }

//FUNKCIJA KOJA CE OTVORITI PROZOR HELP
    @FXML
    public void Test(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("help.fxml"));
            Parent parent = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle("Hee");
            stage.setScene(new Scene(parent, 600, 620));

            stage.setResizable(false);

            stage.show();
        } catch (Exception e) {
            System.out.println("mars nisi me uspio pokreni goni se ");
        }
    }




    @FXML
    public void cut() {
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            // Assuming you want to cut the details of the selected item
            content.putString(selectedItem.getDetalji());
            clipboard.setContent(content);
            // Now you can remove the selected item
            deleteItem(selectedItem);
        }
    }
    @FXML
    public void copy() {
        TodoItem copyte = todoListView.getSelectionModel().getSelectedItem();
        if (copyte != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(copyte.getShortDeskripcija() + "\n" + copyte.getDetalji());
            clipboard.setContent(content);
        }
    }


@FXML
public void paste() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    if (clipboard.hasString()) {
        String pastedText = clipboard.getString();

        // Razdvajamo tekst na redove
        String[] lines = pastedText.split("\n");

        // Prvi red je kratki opis, ostali redovi su detalji
        String shortDescription = lines[0];

        StringBuilder detailsBuilder = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            detailsBuilder.append(lines[i]);
            if (i < lines.length - 1) {
                detailsBuilder.append("\n");
            }
        }
        String details = detailsBuilder.toString();

        // Kreiramo novi TodoItem sa dobivenim informacijama
        TodoItem newItem = new TodoItem(shortDescription, details, LocalDate.now());

        // Dodajemo novi TodoItem u listu
        TodoData.getInstance().addTodoItem(newItem);

        // Ažuriramo prikaz u ListView
        todoListView.getSelectionModel().select(newItem);
    }
}








    @FXML
    public void SaveAs(ActionEvent event) {
        // Obtain the current stage
        Stage stage = (Stage) mainmBorderPain.getScene().getWindow();

        // Creating a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");

        // Adding extension filter for .txt files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Adding extension filter for all files (optional)
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

        // Opening the file chooser dialog
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                // Perform save operation here
                System.out.println("File selected: " + file.getAbsolutePath());
                FileWriter writer = new FileWriter(file);
                ObservableList<TodoItem> items = todoListView.getItems();
                for (TodoItem item : items) {
                    String details = item.getDetalji();
                    String nest = item.getShortDeskripcija();
                    String deadline = item.getDeadLine().toString();
                    writer.write("Podaic: " + nest + "\n");
                    writer.write("Description: " + details + "\n");
                    writer.write("Deadline: " + deadline + "\n");
                    writer.write("------------------------------------\n");
                }
                writer.close(); // Don't forget to close the writer
                System.out.println("File saved successfully.");
            } catch (IOException e) {
                // Handle IO exception
                e.printStackTrace();
                System.out.println("Error saving file: " + e.getMessage());
            }
        }
    }
}
