package com.example.stock.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseContext implements AutoCloseable{

    private final String URL =
            "jdbc:postgresql://localhost:5432/stock";

    private  final String USER =
            "postgres";

    private final String PASSWORD =
            "etu003948";

    public final Connection connection;

    public Connection getConnection() {
        return connection;
    }
    public DatabaseContext() throws SQLException, ClassNotFoundException {
        this.connection = inttConnection();
    }
    public  Connection inttConnection() throws SQLException , ClassNotFoundException {

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
    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}