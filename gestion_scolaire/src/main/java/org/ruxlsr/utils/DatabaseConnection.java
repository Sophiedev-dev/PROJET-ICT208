package org.ruxlsr.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_scolaire?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root"; // change selon ton utilisateur MySQL
    private static final String PASSWORD = "root"; // change selon ton mot de passe

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
