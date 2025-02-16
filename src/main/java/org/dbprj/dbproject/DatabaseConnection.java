package org.dbprj.dbproject;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The DatabaseConnection class is responsible for managing the database connection.
 * It loads database connection properties from the `config.properties` file and
 * provides a method to establish a connection to the database.
 */
public class DatabaseConnection {

    private static Properties config = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            } else {
                // Load the configuration file
                config.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Establishes a connection to the database using the properties from the
     * `config.properties` file.
     *
     * @return A Connection object to the database, or null if the connection fails.
     */
    public static Connection getConnection() {
        String url = config.getProperty("db.url");
        String username = config.getProperty("db.username");
        String password = config.getProperty("db.password");

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Chyba při připojení k databázi.");
            return null;
        }
    }
}
