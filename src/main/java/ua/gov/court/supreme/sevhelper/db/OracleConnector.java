package ua.gov.court.supreme.sevhelper.db;

import ua.gov.court.supreme.sevhelper.exception.DatabaseException;
import ua.gov.court.supreme.sevhelper.service.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnector implements DatabaseConnector {
    private final PropertiesLoader propertiesLoader;

    public OracleConnector() {
        this.propertiesLoader = PropertiesLoader.getInstance();

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Failed to initialize Oracle DB connection", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                propertiesLoader.getOracleUrl(),
                propertiesLoader.getOracleUser(),
                propertiesLoader.getOraclePassword()
        );
    }
}
