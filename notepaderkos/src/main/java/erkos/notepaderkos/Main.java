//package erkos.notepaderkos;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//
//import javafx.scene.image.Image;
//import javafx.stage.Stage;
//
//
//import java.io.IOException;
//
//public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
//
//        primaryStage.setTitle("ErkosNotepad");
//        primaryStage.setScene(new Scene(root, 900, 500));
//        primaryStage.getIcons().add(new Image("file:/C:/Users/Public/Documents/notepaderkos/logo/logo.jpg"));
//
//
//        primaryStage.show();
//
//        primaryStage.setResizable(false);
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void stop() throws Exception {
//        try {
//            TodoData.getInstance().storeTodoItems();
//
//        } catch(IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    @Override
//    public void init() throws Exception {
//        try {
//            TodoData.getInstance().loadTodoItems();
//
//        } catch(IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}

package erkos.notepaderkos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        primaryStage.setTitle("ErkosNotepad");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.getIcons().add(new Image("file:/C:/Users/Public/Documents/notepaderkos/logo/logo.jpg"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        try {
            TodoData.getInstance().storeTodoItems();
        } catch(IOException e) {
            System.out.println("Greška pri spremanju podataka: " + e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch(IOException e) {
            System.out.println("Greška pri učitavanju podataka: " + e.getMessage());
        }
    }
}
