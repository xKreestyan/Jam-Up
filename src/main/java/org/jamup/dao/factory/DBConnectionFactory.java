package org.jamup.dao.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionFactory {

    private static final String URL      = "jdbc:mariadb://localhost:3306/jamup";
    private static final String USER     = "jamup_user";
    private static final String PASSWORD = "jamup_password";

    private static DBConnectionFactory instance;
    private Connection connection;

    private DBConnectionFactory() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static DBConnectionFactory getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DBConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}