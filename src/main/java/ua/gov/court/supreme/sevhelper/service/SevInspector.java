package ua.gov.court.supreme.sevhelper.service;

import ua.gov.court.supreme.sevhelper.db.SevUsersRepository;
import ua.gov.court.supreme.sevhelper.exception.BusinessException;
import ua.gov.court.supreme.sevhelper.exception.DataAccessException;
import ua.gov.court.supreme.sevhelper.exception.FileProcessingException;
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
        try {
            List<String[]> sevUsers = ExcelParser.parseExcel(excelDownloader.downloadFile());

            if (sevUsers.isEmpty()) {
                return;
            }

            sevUsersRepository.dropData();
            sevUsersRepository.saveSevUsersToDB(sevUsers);
            sevUsersRepository.saveDocFlowUsersToDB();
            sevUsersRepository.markUsersConnectedToSev();
            sevUsersRepository.updateTimestamp();
            updateTimeChecker.updateLastUpdateTime();
        } catch (DataAccessException | FileProcessingException e) {
            throw new BusinessException("Can`t garb users data (from url)", e);
        }
    }

    public void grabUserDataFromFile(Part filePart) throws IOException {
        try {
            String fileExtension = filePart.getSubmittedFileName();
            File tempFile = File.createTempFile("upload", fileExtension);
            filePart.write(tempFile.getAbsolutePath());
            List<String[]> sevUsers = ExcelParser.parseExcel(tempFile);
            tempFile.delete();

            if (sevUsers.isEmpty()) {
                return;
            }

            sevUsersRepository.dropData();
            sevUsersRepository.saveSevUsersToDB(sevUsers);
            sevUsersRepository.saveDocFlowUsersToDB();
            sevUsersRepository.markUsersConnectedToSev();
        } catch (DataAccessException | FileProcessingException e) {
            throw new BusinessException("Can`t grab users data (from file)", e);
        }
    }

    public List<SevUser> getUserData() {
        try {
            return sevUsersRepository.getAllData();
        } catch (DataAccessException e) {
            throw new BusinessException("Can`t get users data from database", e);
        }
    }

    public LocalDateTime getLastUpdateTimestamp() {
        try {
            return sevUsersRepository.getLastUpdateTimestamp();
        } catch (DataAccessException e) {
            throw new BusinessException("Can`t get last update timestamp from database", e);
        }
    }

    public boolean canGrabUserData() {
        try {
            return updateTimeChecker.canUpdate();
        } catch (DataAccessException e) {
            throw new BusinessException("Can`t check last update timestamp from database", e);
        }
    }
}
