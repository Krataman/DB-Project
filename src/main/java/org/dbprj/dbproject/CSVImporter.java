package org.dbprj.dbproject;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.scene.control.Alert;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.dbprj.dbproject.AlertHelper.showAlert;

public class CSVImporter {
    static void importCSVProducts(Path filePath) {
        int importedCount = 0;
        int skippedCount = 0;

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath.toFile()))) {
            String[] row;
            boolean isHeader = true;

            while ((row = csvReader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (row.length < 4) {
                    skippedCount++;
                    continue;
                }

                String name = row[0];
                float price;
                try {
                    price = Float.parseFloat(row[1]);
                } catch (NumberFormatException e) {
                    skippedCount++;
                    continue;
                }
                String description = row[2];
                int categoryId;

                try {
                    categoryId = Integer.parseInt(row[3]);
                } catch (NumberFormatException e) {
                    skippedCount++;
                    continue;
                }

                boolean inserted = ProductManagement.insertProduct(name, price, description, true, categoryId);
                if (inserted) {
                    importedCount++;
                } else {
                    skippedCount++;
                }
            }

            // Zobrazení informačního okna po importu
            showAlert(Alert.AlertType.INFORMATION, "Import dokončen",
                    "Úspěšně naimportováno: " + importedCount + " produktů\n" +
                            "Přeskočeno: " + skippedCount + " řádků (chybná data)");

        } catch (IOException | CsvValidationException e) {
            showAlert(Alert.AlertType.ERROR, "Chyba při importu",
                    "Nepodařilo se načíst soubor: " + e.getMessage());
        }
    }

}
