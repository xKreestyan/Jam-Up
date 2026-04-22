package org.jamup.dao.factory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.jamup.exception.DAOException;

public class DBConnectionFactory {

    //thread-safe initialization
    private static class ConnectionHolder {
        private static final Connection connection;

        static {
            try (InputStream input = DBConnectionFactory.class.getClassLoader()
                    .getResourceAsStream("org/jamup/dao/db/db.properties")) {
                Properties properties = new Properties();
                properties.load(input);

                String url  = properties.getProperty("CONNECTION_URL");
                String user = properties.getProperty("LOGIN_USER");
                String pass = properties.getProperty("LOGIN_PASS");

                connection = DriverManager.getConnection(url, user, pass);
            } catch (IOException | SQLException e) {
                throw new DAOException("Failed to initialize DB connection", e);
            }
        }
    }

    private DBConnectionFactory() {}

    public static Connection getConnection() {
        return ConnectionHolder.connection;
    }

}