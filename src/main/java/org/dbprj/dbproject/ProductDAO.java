package org.dbprj.dbproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO {

    public static void insertProduct(String name, float price, String description, boolean inStock, int categoryId) {
        String sql = "INSERT INTO Products (name, description, price, in_stock, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);  // Popis
            statement.setFloat(3, price);         // Cena
            statement.setBoolean(4, inStock);     // Stav skladu (in_stock)
            statement.setInt(5, categoryId);      // ID kategorie
            statement.executeUpdate();
            System.out.println("Produkt byl úspěšně přidán.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chyba při vkládání produktu.");
        }
    }


    public static void getAllProducts() {
        String sql = "SELECT * FROM Products";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name"); // Změněno na "name"
                float price = resultSet.getFloat("price");
                String description = resultSet.getString("description");
                System.out.println("Produkt: " + name + ", Cena: " + price + ", Popis: " + description);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chyba při získávání produktů.");
        }
    }

}
