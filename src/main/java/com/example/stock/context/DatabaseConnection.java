package com.example.stock.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/stock";

    private static final String USER =
            "postgres";

    private static final String PASSWORD =
            "postgres";

    public static Connection getConnection() throws Exception {

        try {

            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (ClassNotFoundException | SQLException e) {

            throw new Exception(
                    "Erreur de connexion à PostgreSQL",
                    e
            );
        }
    }
}