package org.dbprj.dbproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductApp extends Application {

    @Override
    public void start(Stage stage) {
        // Vytvoření GUI komponent
        TextField nameField = new TextField();
        nameField.setPromptText("Zadejte název produktu");

        TextField priceField = new TextField();
        priceField.setPromptText("Zadejte cenu produktu");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Zadejte popis produktu");

        Button addButton = new Button("Přidat produkt");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            float price = Float.parseFloat(priceField.getText());
            String description = descriptionField.getText();
            boolean inStock = true;  // Výchozí hodnota pro in_stock
            int categoryId = 1;      // Výchozí hodnota pro category_id

            ProductDAO.insertProduct(name, price, description, inStock, categoryId);
        });

        // Tlačítko pro import CSV souboru s produkty
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
        layout.getChildren().addAll(nameField, priceField, descriptionField, addButton, importButton);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Produktová aplikace");
        stage.show();
    }

    // Metoda pro importování dat z CSV pro produkty
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
                float price = 0;
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

    public static void main(String[] args) {
        launch(args);
    }
}

