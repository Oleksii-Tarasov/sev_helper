package ua.gov.court.supreme.sevhelper.db;

import ua.gov.court.supreme.sevhelper.exception.DatabaseException;
import ua.gov.court.supreme.sevhelper.service.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnector implements DatabaseConnector {
    public final PropertiesLoader propertiesLoader;

    public PostgresConnector() {
        this.propertiesLoader = PropertiesLoader.getInstance();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Failed to initialize Postgres DB connection", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                propertiesLoader.getPostgresUrl(),
                propertiesLoader.getPostgresUser(),
                propertiesLoader.getPostgresPassword()
        );
    }
}
