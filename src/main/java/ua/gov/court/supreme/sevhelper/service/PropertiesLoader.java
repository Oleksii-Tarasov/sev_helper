package ua.gov.court.supreme.sevhelper.service;

import ua.gov.court.supreme.sevhelper.exception.FileProcessingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PropertiesLoader {
    private static final Properties properties = new Properties();
    private static PropertiesLoader instance;

    public PropertiesLoader() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load configuration file.", e);
        }
    }

    public static synchronized PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    //    Oracle DB
    public String getOracleUrl() {
        return getProperty("db.url");
    }

    public String getOracleUser() {
        return getProperty("db.user");
    }

    public String getOraclePassword() {
        return getProperty("db.password");
    }

    //    Postgres DB
    public String getPostgresUrl() {
        return getProperty("postgres.db.url");
    }

    public String getPostgresUser() {
        return getProperty("postgres.db.user");
    }

    public String getPostgresPassword() {
        return getProperty("postgres.db.password");
    }

    //    Other
    public String getFileUrl() {
        return getProperty("file.url");
    }

    public String getSchedulerCronExpression() {
        return getProperty("scheduler.cron.expression");
    }

    public List<String> getExpectedExcelHeaders() {
        String headersFromProperties = getProperty("excel.headers");

        if (headersFromProperties == null || headersFromProperties.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.asList(headersFromProperties.split(","));
    }
}
