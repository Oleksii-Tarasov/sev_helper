package ua.gov.court.supreme.sevhelper.service;

import ua.gov.court.supreme.sevhelper.exception.FileProcessingException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class ExcelDownloader {
    private final PropertiesLoader propertiesLoader;

    public ExcelDownloader() {
        this.propertiesLoader = PropertiesLoader.getInstance();
    }

    public File downloadFile() throws IOException {
        URL url = new URL(propertiesLoader.getFileUrl());
        File sevUsersFile = Files.createTempFile("sev_users", ".xlsx").toFile();

        try (InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(sevUsersFile)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new FileProcessingException("Can`t download file from URL: " + url, e);
        }

        return sevUsersFile;
    }
}
