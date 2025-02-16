package org.dbprj.dbproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing products in the database.
 * Provides methods for inserting, deleting, and retrieving products.
 */
public class ProductDAO {

    /**
     * Inserts a new product into the database.
     *
     * @param name The name of the product.
     * @param price The price of the product.
     * @param description A description of the product.
     * @param inStock Indicates whether the product is in stock.
     * @param categoryId The ID of the product's category.
     */
    public static void insertProduct(String name, float price, String description, boolean inStock, int categoryId) {
        if (!categoryExists(categoryId)) {
            System.out.println("Chyba: Kategorie s ID " + categoryId + " neexistuje.");
            return;
        }

        String sql = "INSERT INTO Products (name, description, price, in_stock, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setFloat(3, price);
            statement.setBoolean(4, inStock);
            statement.setInt(5, categoryId);
            statement.executeUpdate();
            System.out.println("Produkt byl úspěšně přidán.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chyba při vkládání produktu.");
        }
    }

    /**
     * Retrieves all products from the database and formats them into a string.
     *
     * @return A string representing all products in the database.
     */
    public static String getAllProducts() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM Products";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                float price = resultSet.getFloat("price");
                String description = resultSet.getString("description");
                sb.append("ID: ").append(id)
                        .append(" | Produkt: ").append(name)
                        .append(" | Cena: ").append(price)
                        .append(" | Popis: ").append(description)
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Chyba při získávání produktů.";
        }
        return sb.toString();
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param productId The ID of the product to delete.
     * @return true if the product was successfully deleted, false otherwise.
     */
    public static boolean deleteProductById(int productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0; // Returns true if a row was deleted, otherwise false

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a category exists in the database by its ID.
     *
     * @param categoryId The ID of the category to check.
     * @return true if the category exists, false otherwise.
     */
    public static boolean categoryExists(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Categories WHERE category_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
