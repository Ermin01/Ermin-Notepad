module erkos.notepaderkos {
    requires javafx.controls;
    requires javafx.fxml;


    opens erkos.notepaderkos to javafx.fxml;
    exports erkos.notepaderkos;
}