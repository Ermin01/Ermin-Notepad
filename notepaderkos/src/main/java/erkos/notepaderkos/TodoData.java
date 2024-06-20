//package erkos.notepaderkos;
//
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Iterator;
//import java.util.List;
//public class TodoData {
//    private static TodoData instance = new TodoData();
//    private static String filename = "TodoListItems.txt";
//
//    private ObservableList<TodoItem> todoItems;
//    private DateTimeFormatter formatter;
//
//    public static TodoData getInstance() {
//        return instance;
//    }
//
//    private TodoData() {
//        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//    }
//
//    public ObservableList<TodoItem> getTodoItems() {
//        return todoItems;
//    }
//
////        public void setTodoItems(List<TodoItem> todoItems) {
////        this.todoItems = todoItems;
////    }
//
//    public void addTodoItem(TodoItem item) {
//        todoItems.add(item);
//    }
//
//    public void loadTodoItems() throws IOException {
//        todoItems = FXCollections.observableArrayList();
//        Path path = Paths.get(filename);
//        BufferedReader br = Files.newBufferedReader(path);
//
//        String input;
//
//        try {
//            while ((input = br.readLine()) != null) {
//                // Split the input line using tab character ("\t")
//                String[] itemPieces = input.split("\t");
//
//                // Check if the split produced enough elements
//                if (itemPieces.length >= 3) { // Ensure at least 3 elements for short description, details, and date
//                    String shortDescription = itemPieces[0];
//                    String details = itemPieces[1];
//                    String dateString = itemPieces[2];
//
//                    LocalDate date = LocalDate.parse(dateString, formatter);
//                    TodoItem todoItem = new TodoItem(shortDescription, details, date);
//                    todoItems.add(todoItem);
//                } else {
//                    // Handle invalid input format or skip this line
//                    System.err.println("Invalid input format: " + input);
//                    // You may choose to skip this line or handle it differently based on your requirements
//                }
//            }
//        } finally {
//            if(br != null) {
//                br.close();
//            }
//        }
//    }
//
//
//
//    public void storeTodoItems() throws IOException {
//
//        Path path = Paths.get(filename);
//        BufferedWriter bw = Files.newBufferedWriter(path);
//        try {
//            Iterator<TodoItem> iter = todoItems.iterator();
//            while(iter.hasNext()) {
//                TodoItem item = iter.next();
//                bw.write(String.format("%s\t%s\t%s",
//                        item.getShortDeskripcija(),
//                        item.getDetalji(),
//                        item.getDeadLine().format(formatter)));
//                bw.newLine();
//            }
//
//        } finally {
//            if(bw != null) {
//                bw.close();
//            }
//        }
//    }
//
//
//    public void deleteItem(TodoItem item){
//        todoItems.remove(item);
//    }
//}

package erkos.notepaderkos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;

public class TodoData {
    private static TodoData instance = new TodoData();
    private static String filename = "TodoListItems.txt";

    private ObservableList<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoData getInstance() {
        return instance;
    }

    private TodoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        todoItems = FXCollections.observableArrayList(); // Inicijalizacija todoItems
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void addTodoItem(TodoItem item) {
        if (todoItems == null) {
            todoItems = FXCollections.observableArrayList(); // Inicijalizacija todoItems ako je null
        }
        todoItems.add(item);
    }

    public void loadTodoItems() throws IOException {
        todoItems.clear(); // Očisti listu prije učitavanja novih stavki
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                if (itemPieces.length >= 3) {
                    String shortDescription = itemPieces[0];
                    String details = itemPieces[1];
                    String dateString = itemPieces[2];

                    try {
                        LocalDate date = LocalDate.parse(dateString, formatter);
                        TodoItem todoItem = new TodoItem(shortDescription, details, date);
                        todoItems.add(todoItem);
                    } catch (DateTimeParseException e) {
                        System.err.println("Error parsing date for item: " + input);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Invalid input format: " + input);
                }
            }
        } finally {
            if(br != null) {
                br.close();
            }
        }
    }

    public void storeTodoItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<TodoItem> iter = todoItems.iterator();
            while(iter.hasNext()) {
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDeskripcija(),
                        item.getDetalji(),
                        item.getDeadLine().format(formatter)));
                bw.newLine();
            }
        } finally {
            if(bw != null) {
                bw.close();
            }
        }
    }

    public void deleteItem(TodoItem item){
        todoItems.remove(item);
    }
}
