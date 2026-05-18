package com.example.stock.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseContext implements AutoCloseable{

    private static final String URL =
            "jdbc:postgresql://localhost:5432/stock";

    private  static final String USER =
            "postgres";

    private static final String PASSWORD =
            "etu003948";

    
    
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        return createNewConnection();
    }
     
    public static Connection createNewConnection() throws SQLException, ClassNotFoundException {
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
        //TODO :
    }
}