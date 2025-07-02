package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_scolaire?createDatabaseIfNotExist=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connexion;

    public static Connection getConnexion() throws SQLException {
        if (connexion == null || connexion.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connexion = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL introuvable.", e);
            }
        }
        return connexion;
    }
}
