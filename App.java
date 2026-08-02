package com.mycompany.project2.month1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {

    private Library lib = new Library();
    private TableView<Book> table = new TableView<>();
    private ObservableList<Book> obsList;

    private Label lTotal = new Label("Total Titles: 0");
    private Label lStock = new Label("Total Copies in Stock: 0");

    @Override
    public void start(Stage stage) {
        lib.loadData();
        obsList = FXCollections.observableArrayList(lib.getBooks());

        stage.setTitle("Library Management"
                + " System");

        TableColumn<Book, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(new PropertyValueFactory<>("bookID"));

        TableColumn<Book, String> c2 = new TableColumn<>("Title");
        c2.setCellValueFactory(new PropertyValueFactory<>("title"));
        c2.setPrefWidth(160);

        TableColumn<Book, String> c3 = new TableColumn<>("Author");
        c3.setCellValueFactory(new PropertyValueFactory<>("author"));
        c3.setPrefWidth(130);

        TableColumn<Book, String> c4 = new TableColumn<>("ISBN");
        c4.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        TableColumn<Book, Integer> c5 = new TableColumn<>("Quantity");
        c5.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().addAll(c1, c2, c3, c4, c5);
        table.setItems(obsList);

        TextField tfSearch = new TextField();
        tfSearch.setPromptText("Search by Title or ISBN");
        tfSearch.setPrefWidth(200);

        Button bSearch = new Button("Search");
        bSearch.setOnAction(e -> {
            String q = tfSearch.getText();
            Book found = lib.searchByISBN(q);
            if (found == null) found = lib.searchByTitle(q);

            if (found != null) {
                table.getSelectionModel().select(found);
                showAlert(Alert.AlertType.INFORMATION, "Found", "Book: " + found.getTitle() + " | Stock: " + found.getQuantity());
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found", "No matching book found.");
            }
        });

        Button bReset = new Button("Reset Table");
        bReset.setOnAction(e -> {
            tfSearch.clear();
            refreshData();
        });

        HBox hb1 = new HBox(10, tfSearch, bSearch, bReset);
        hb1.setPadding(new Insets(10));
        hb1.setAlignment(Pos.CENTER_LEFT);

        VBox v1 = new VBox(10, hb1, table);
        v1.setPadding(new Insets(10));

        Tab tab1 = new Tab("Book Inventory", v1);
        tab1.setClosable(false);

        Label l1 = new Label("Add New Book to System");
        l1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField t1 = new TextField(); t1.setPromptText("Book Title");
        TextField t2 = new TextField(); t2.setPromptText("Author Name");
        TextField t3 = new TextField(); t3.setPromptText("ISBN Code");
        TextField t4 = new TextField(); t4.setPromptText("Initial Quantity");

        Button b1 = new Button("Add Book");
        b1.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold;");
        b1.setOnAction(e -> {
            try {
                String title = t1.getText();
                String author = t2.getText();
                String isbn = t3.getText();
                int qty = Integer.parseInt(t4.getText());

                if (!title.isEmpty() && !author.isEmpty() && !isbn.isEmpty()) {
                    lib.addBook(new Book(title, author, isbn, qty));
                    refreshData();
                    t1.clear(); t2.clear(); t3.clear(); t4.clear();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "New book added successfully!");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Error", "Please fill in all fields.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be a valid number.");
            }
        });

        VBox v2 = new VBox(12, l1, t1, t2, t3, t4, b1);
        v2.setPadding(new Insets(20));
        v2.setMaxWidth(350);
        v2.setAlignment(Pos.CENTER);

        Tab tab2 = new Tab("Add Book", v2);
        tab2.setClosable(false);

        Label l2 = new Label("Issue or Return Books");
        l2.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField t5 = new TextField();
        t5.setPromptText("Enter Title or ISBN");
        t5.setPrefWidth(250);

        Button b2 = new Button("Issue Book");
        b2.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white;");
        b2.setOnAction(e -> {
            if (lib.issueBook(t5.getText())) {
                refreshData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book issued successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Book not found or out of stock.");
            }
        });

        Button b3 = new Button("Return Book");
        b3.setStyle("-fx-background-color: #ed6c02; -fx-text-fill: white;");
        b3.setOnAction(e -> {
            if (lib.returnBook(t5.getText())) {
                refreshData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book returned successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Book not found.");
            }
        });

        HBox hb2 = new HBox(10, b2, b3);
        hb2.setAlignment(Pos.CENTER);

        VBox v3 = new VBox(15, l2, t5, hb2);
        v3.setPadding(new Insets(20));
        v3.setMaxWidth(350);
        v3.setAlignment(Pos.CENTER);

        Tab tab3 = new Tab("Issue / Return", v3);
        tab3.setClosable(false);

        Label l3 = new Label("Library Overview & Statistics");
        l3.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        lTotal.setStyle("-fx-font-size: 14px;");
        lStock.setStyle("-fx-font-size: 14px;");

        VBox v4 = new VBox(15, l3, lTotal, lStock);
        v4.setPadding(new Insets(30));
        v4.setAlignment(Pos.TOP_LEFT);

        Tab tab4 = new Tab("Dashboard", v4);
        tab4.setClosable(false);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);

        refreshData();

        Scene scene = new Scene(tabPane, 650, 480);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> lib.saveData());
    }

    private void refreshData() {
        obsList.setAll(lib.getBooks());

        int totalTitles = lib.getBooks().size();
        int totalCopies = 0;
        for (Book b : lib.getBooks()) {
            totalCopies += b.getQuantity();
        }
        lTotal.setText("Total Titles Registered: " + totalTitles);
        lStock.setText("Total Copies in Stock: " + totalCopies);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
