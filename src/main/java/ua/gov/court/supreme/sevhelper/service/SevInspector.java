package ua.gov.court.supreme.sevhelper.service;

import ua.gov.court.supreme.sevhelper.db.SevUsersRepository;
import ua.gov.court.supreme.sevhelper.model.SevUser;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class SevInspector {
    private final ExcelDownloader excelDownloader;
    private final SevUsersRepository sevUsersRepository;
    private final UpdateTimeChecker updateTimeChecker;

    public SevInspector() {
        this.sevUsersRepository = new SevUsersRepository();
        this.updateTimeChecker = new UpdateTimeChecker();
        this.excelDownloader = new ExcelDownloader();
    }

    public void grabUserDataFromUrl() throws IOException {
        List<String[]> sevUsers = ExcelParser.parseExcel(excelDownloader.downloadFile());

        sevUsersRepository.dropData();
        sevUsersRepository.saveSevUsersToDB(sevUsers);
        sevUsersRepository.saveDocFlowUsersToDB();
        sevUsersRepository.markUsersConnectedToSev();
        sevUsersRepository.updateTimestamp();
        updateTimeChecker.updateLastUpdateTime();
    }

    public void grabUserDataFromFile(Part filePart) throws IOException {
        String fileExtension = filePart.getSubmittedFileName();
        File tempFile = File.createTempFile("upload", fileExtension);
        filePart.write(tempFile.getAbsolutePath());
        List<String[]> sevUsers = ExcelParser.parseExcel(tempFile);
        tempFile.delete();

        sevUsersRepository.dropData();
        sevUsersRepository.saveSevUsersToDB(sevUsers);
        sevUsersRepository.saveDocFlowUsersToDB();
        sevUsersRepository.markUsersConnectedToSev();
    }


    public List<SevUser> getUserData() {
        return sevUsersRepository.getAllData();
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return sevUsersRepository.getLastUpdateTimestamp();
    }

    public boolean canGrabUserData() {
        return updateTimeChecker.canUpdate();
    }
}
