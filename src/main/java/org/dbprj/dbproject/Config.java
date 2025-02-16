package org.dbprj.dbproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The Config class is responsible for loading configuration properties from the
 * `config.properties` file and providing access to these properties such as
 * the database URL, username, and password.
 */
public class Config {
    private static Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            // Loads the properties from the configuration file
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Chyba při načítání konfiguračního souboru.");
        }
    }

    /**
     * Retrieves the database URL from the configuration file.
     *
     * @return The database URL as a string.
     */
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    /**
     * Retrieves the database username from the configuration file.
     *
     * @return The database username as a string.
     */
    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    /**
     * Retrieves the database password from the configuration file.
     *
     * @return The database password as a string.
     */
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
}
