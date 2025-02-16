package org.dbprj.dbproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Main entry point of the ProductApp application.
 * This class provides functionality to add, delete, view, and import products via a GUI.
 */
public class ProductApp extends Application {

    /**
     * Initializes the main stage and sets up the tabs for the application.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        // Vytvoření tabů
        TabPane tabPane = new TabPane();

        // Tab pro přidání produktu
        Tab addProductTab = new Tab("Přidat produkt", createAddProductTab(stage));

        // Tab pro mazání produktů
        Tab deleteProductTab = new Tab("Smazat produkt", createDeleteProductTab());

        // Tab pro získání produktů
        Tab getProductTab = new Tab("Získat produkty", createGetProductTab());

        tabPane.getTabs().addAll(addProductTab, deleteProductTab, getProductTab);

        Scene scene = new Scene(tabPane, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Produktová aplikace");
        stage.show();
    }

    /**
     * Creates the layout for the "Add Product" tab, including input fields and buttons.
     *
     * @param stage The primary stage for the application.
     * @return VBox containing the layout for adding a product.
     */
    private VBox createAddProductTab(Stage stage) {
        TextField nameField = new TextField();
        nameField.setPromptText("Zadejte název produktu");

        TextField priceField = new TextField();
        priceField.setPromptText("Zadejte cenu produktu");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Zadejte popis produktu");

        TextField categoryID = new TextField();
        categoryID.setPromptText("Zadejte ID kategorie");

        Button addButton = new Button("Přidat produkt");
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                float price = Float.parseFloat(priceField.getText());
                String description = descriptionField.getText();
                int categoryId = Integer.parseInt(categoryID.getText());

                if (!ProductDAO.categoryExists(categoryId)) {
                    showAlert(Alert.AlertType.ERROR, "Error!", "Kategorie s ID " + categoryId + " neexistuje.");
                    return;
                }

                ProductDAO.insertProduct(name, price, description, true, categoryId);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error!", "Zadejte platné číselné hodnoty pro cenu a ID kategorie.");
            }
        });

        Button importButton = new Button("Importovat produkty");
        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            Path filePath = fileChooser.showOpenDialog(stage).toPath();

            if (filePath != null) {
                importCSVProducts(filePath);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameField, priceField, descriptionField, categoryID, addButton, importButton);
        return layout;
    }

    /**
     * Creates the layout for the "Delete Product" tab, including input fields and buttons.
     *
     * @return VBox containing the layout for deleting a product.
     */
    private VBox createDeleteProductTab() {
        TextField idField = new TextField();
        idField.setPromptText("Zadejte ID produktu ke smazání");

        Button deleteButton = new Button("Smazat produkt");
        deleteButton.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(idField.getText());
                boolean deleted = ProductDAO.deleteProductById(productId);

                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Úspěch", "Produkt byl úspěšně smazán.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error!", "Produkt s tímto ID neexistuje.");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error!", "Zadejte platné číselné ID produktu.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(idField, deleteButton);
        return layout;
    }

    /**
     * Creates the layout for the "Get Product" tab, including a button to display products.
     *
     * @return VBox containing the layout for viewing products.
     */
    private VBox createGetProductTab() {
        Button getProductsButton = new Button("Zobrazit produkty");
        TextArea productList = new TextArea();
        productList.setEditable(false);

        getProductsButton.setOnAction(e -> {
            productList.setText(ProductDAO.getAllProducts());
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(getProductsButton, productList);
        return layout;
    }

    /**
     * Imports product data from a CSV file into the database.
     *
     * @param filePath The path to the CSV file to import.
     */
    private void importCSVProducts(Path filePath) {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath.toFile()))) {
            String[] row;
            boolean isHeader = true;

            while ((row = csvReader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (row.length < 4) {
                    System.out.println("Skipping invalid row: " + String.join(", ", row));
                    continue;
                }

                String name = row[0];
                float price;
                try {
                    price = Float.parseFloat(row[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price: " + row[1]);
                    continue;
                }
                String description = row[2];
                int categoryId = Integer.parseInt(row[3]);

                ProductDAO.insertProduct(name, price, description, true, categoryId);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays an alert to the user.
     *
     * @param type The type of the alert (ERROR, INFORMATION, etc.).
     * @param title The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
