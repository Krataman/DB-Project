package org.dbprj.dbproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.nio.file.Path;

public class ProductApp extends Application {

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab addProductTab = new Tab("Přidat produkt", createAddProductTab(stage));
        Tab deleteProductTab = new Tab("Smazat produkt", createDeleteProductTab());
        Tab getProductTab = new Tab("Získat produkty", createGetProductTab());

        tabPane.getTabs().addAll(addProductTab, deleteProductTab, getProductTab);

        Scene scene = new Scene(tabPane, 600, 600);
        stage.setScene(scene);
        stage.setTitle("Produktová aplikace");
        stage.show();
    }

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

                if (!ProductManagement.categoryExists(categoryId)) {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, "Chyba!", "Kategorie s ID " + categoryId + " neexistuje.");
                    return;
                }

                boolean inserted = ProductManagement.insertProduct(name, price, description, true, categoryId);

                if (inserted) {
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Úspěch", "Produkt byl úspěšně přidán.");
                } else {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, "Chyba", "Nepodařilo se přidat produkt.");
                }
            } catch (NumberFormatException ex) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Chyba vstupu", "Zadejte platné číselné hodnoty pro cenu a ID kategorie.");
            }
        });

        Button importButton = new Button("Importovat produkty");
        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            Path filePath = fileChooser.showOpenDialog(stage).toPath();

            if (filePath != null) {
                CSVImporter.importCSVProducts(filePath);
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(nameField, priceField, descriptionField, categoryID, addButton, importButton);
        return layout;
    }

    private VBox createDeleteProductTab() {
        TextField idField = new TextField();
        idField.setPromptText("Zadejte ID produktu ke smazání");

        Button deleteButton = new Button("Smazat produkt");
        deleteButton.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(idField.getText());
                boolean deleted = ProductManagement.deleteProductById(productId);

                if (deleted) {
                    AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Úspěch", "Produkt byl úspěšně smazán.");
                } else {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, "Chyba!", "Produkt s tímto ID neexistuje.");
                }
            } catch (NumberFormatException ex) {
                AlertHelper.showAlert(Alert.AlertType.ERROR, "Chyba!", "Zadejte platné číselné ID produktu.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(idField, deleteButton);
        return layout;
    }

    private VBox createGetProductTab() {
        Button getProductsButton = new Button("Zobrazit produkty");
        TextArea productList = new TextArea();
        productList.setEditable(false);

        getProductsButton.setOnAction(e -> {
            productList.setText(ProductManagement.getAllProducts());
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(getProductsButton, productList);
        return layout;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
