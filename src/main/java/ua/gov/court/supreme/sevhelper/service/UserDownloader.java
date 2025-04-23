package ua.gov.court.supreme.sevhelper.service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class UserDownloader {
    private static final String fileURL = "https://se.diia.gov.ua/uploads/documents/45.xlsx";

    public static void downloadFile() throws IOException {
        URL url = new URL(fileURL);
        File tempFile = Files.createTempFile("sev_users", ".xlsx").toFile();

        try (InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
    }
}
