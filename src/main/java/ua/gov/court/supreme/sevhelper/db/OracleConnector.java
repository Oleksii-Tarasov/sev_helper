package ua.gov.court.supreme.sevhelper.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OracleConnector implements DatabaseConnector {
    private static final Properties properties = new Properties();

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            properties.load(OracleConnector.class.getClassLoader()
                            .getResourceAsStream("application.properties"));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Failed to initialize Oracle DB connection", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
