package org.jamup.dao.factory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.jamup.exception.DAOException;

public class DBConnectionFactory {

    private static final Connection connection;

    private DBConnectionFactory() {}

    static {
        try (InputStream input = DBConnectionFactory.class.getClassLoader()
                .getResourceAsStream("org/jamup/dao/db/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String url  = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty("LOGIN_USER");
            String pass = properties.getProperty("LOGIN_PASS");

            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connection established");
        } catch (IOException | SQLException e) {
            throw new DAOException("Failed to initialize DB connection", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}