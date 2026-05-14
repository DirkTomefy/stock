package com.example.stock.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseContext {

    private final String URL =
            "jdbc:postgresql://localhost:5432/stock";

    private  final String USER =
            "postgres";

    private final String PASSWORD =
            "postgres";

    public  Connection getConnection() throws SQLException , ClassNotFoundException {

        try {

            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

        } catch (ClassNotFoundException | SQLException e) {

            throw e;
          
        }
    }
}