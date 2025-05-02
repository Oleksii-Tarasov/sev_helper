package ua.gov.court.supreme.sevhelper.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresConnector implements DatabaseConnector {
    public static final Properties properties = new Properties();

    static {
        try {
            Class.forName("org.postgresql.Driver");
            properties.load(PostgresConnector.class.getClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Failed to initialize Postgres DB connection", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("postgres.db.url"),
                properties.getProperty("postgres.db.user"),
                properties.getProperty("postgres.db.password")
        );
    }
}
