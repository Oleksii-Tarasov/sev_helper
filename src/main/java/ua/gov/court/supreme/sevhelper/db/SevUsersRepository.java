package ua.gov.court.supreme.sevhelper.db;

import ua.gov.court.supreme.sevhelper.exception.DataAccessException;
import ua.gov.court.supreme.sevhelper.exception.DatabaseException;
import ua.gov.court.supreme.sevhelper.model.SevUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SevUsersRepository {
    private final DatabaseConnector postgresConnector;
    private final DatabaseConnector oracleConnector;

    public SevUsersRepository() {
        try {
            this.postgresConnector = new PostgresConnector();
            this.oracleConnector = new OracleConnector();
        } catch (DatabaseException e) {
            throw new DataAccessException("Failed to initialize DB connectors", e);
        }
    }

    public void saveSevUsersToDB(List<String[]> sevUsers) {
        String query = """
                INSERT INTO SEV_USERS (edrpou, short_name, full_name, is_terminated) 
                VALUES (?, ?, ?, ?)
                ON CONFLICT (edrpou) DO UPDATE 
                SET short_name = EXCLUDED.short_name, 
                    full_name = EXCLUDED.full_name, 
                    is_terminated = EXCLUDED.is_terminated
                """;

        try (Connection connection = postgresConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (String[] user : sevUsers) {
                statement.setString(1, user[0]); // edrpou
                statement.setString(2, user[1]); // short_name
                statement.setString(3, user[2]); // full_name
                statement.setString(4, user[3]); // is_terminated
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new DataAccessException("Error saving SEV users to Postgres database", e);
        }
    }

    // DocFlow EDRPOU
    public void saveDocFlowUsersToDB() {
        List<String> docFlowUsers = getDocFlowSevUsers();
        String query = """
                INSERT INTO docflow_users (edrpou) 
                VALUES (?)
                ON CONFLICT (edrpou) DO NOTHING
                """;

        try (Connection connection = postgresConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (String edrpou : docFlowUsers) {
                statement.setString(1, edrpou);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            throw new DataAccessException("Error saving DocFlow users to Postgres database", e);
        }
    }

    private List<String> getDocFlowSevUsers() {
        List<String> docFlowSevUsers = new ArrayList<>();
//        CODOKPO - ЄДРПОУ;
//        ID105_UNIT = 'dblink://' - Тип взаємодії "Посилання";
//        USE_MULTY_OUT_UNIT = 0 - Використовувати загальні налаштування;
//        DOC_MSG_OUT = 3 - Ознака відправлення електронних додатків;
        String query = """
                SELECT DISTINCT t.CODOKPO
                FROM DOCFLOW_VS.TUNIT t
                INNER JOIN DOCFLOW_VS.TMESSAGE_POST mp ON t.IDUNIT = mp.IDUNIT_MSG
                WHERE t.CODOKPO IS NOT NULL
                AND t.ID105_UNIT = 'dblink://'
                AND t.USE_MULTY_OUT_UNIT = 0
                AND mp.DOC_MSG_OUT = 3
                """;

        try (Connection connection = oracleConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                docFlowSevUsers.add(resultSet.getString("CODOKPO"));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving DocFlow users from Oracle database", e);
        }

        return docFlowSevUsers;
    }

    public void markUsersConnectedToSev() {
        String query = """
                UPDATE sev_users su
                SET is_connected = EXISTS (
                    SELECT 1
                    FROM docflow_users du
                    WHERE du.edrpou = su.edrpou
                    )
                """;

        try (Connection connection = postgresConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating SEV users connection status to Postgres database", e);
        }
    }

    public List<SevUser> getAllData() {
        String query = "SELECT * FROM sev_users ORDER BY edrpou";
        List<SevUser> sevUsers = new ArrayList<>();

        try (Connection connection = postgresConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                sevUsers.add(new SevUser(
                        resultSet.getLong("id"),
                        resultSet.getString("edrpou"),
                        resultSet.getString("short_name"),
                        resultSet.getString("full_name"),
                        resultSet.getString("is_terminated"),
                        resultSet.getBoolean("is_connected")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error receiving SEV user data from Postgres database", e);
        }

        return sevUsers;
    }

    public void updateTimestamp() {
        String query = "INSERT INTO update_timestamps (last_update) VALUES (CURRENT_TIMESTAMP)";

        try (Connection connection = postgresConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error updating timestamp", e);
        }
    }

    public LocalDateTime getLastUpdateTimestamp() {
        String query = "SELECT last_update FROM update_timestamps ORDER BY last_update DESC LIMIT 1";

        try (Connection connection = postgresConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getTimestamp("last_update").toLocalDateTime();
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to get last update timestamp from Postgres database", e);
        }
    }

    public void dropData() {
        String query = "TRUNCATE TABLE sev_users, docflow_users";

        try (Connection connection = postgresConnector.getConnection()) {
            connection.prepareStatement(query).execute();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting data from Postgres database", e);
        }
    }
}
