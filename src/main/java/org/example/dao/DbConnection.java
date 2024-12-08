package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
MYSQL_ROOT_PASSWORD
MYSQL_ALLOW_EMPTY_PASSWORD
MYSQL_RANDOM_ROOT_PASSWORD
 */

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";  // Alterar conforme sua senha no MySQL

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Erro na conexão com o banco de dados", e);
        }
    }
}
