package ua.gov.court.supreme.sevhelper.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SevInspector {
    public static List<String> getDocFlowSevUsers() {
        List<String> docFlowSevUsers = new ArrayList<>();
//        CODOKPO - ЄРДПОУ;
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

        try (Connection connection = DBConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                docFlowSevUsers.add(resultSet.getString("CODOKPO"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return docFlowSevUsers;
    }
}
