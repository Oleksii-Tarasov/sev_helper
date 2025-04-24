package ua.gov.court.supreme.sevhelper.service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class ExcelDownloader {
    private static final String fileURL = "https://se.diia.gov.ua/uploads/documents/45.xlsx";

    public static File downloadFile() throws IOException {
        URL url = new URL(fileURL);
        File sevUsersFile = Files.createTempFile("sev_users", ".xlsx").toFile();

        try (InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(sevUsersFile)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }

        return sevUsersFile;
    }
}
