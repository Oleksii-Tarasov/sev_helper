package ua.gov.court.supreme.sevhelper.service;

import ua.gov.court.supreme.sevhelper.service.db.SevUsersRepository;
import ua.gov.court.supreme.sevhelper.service.model.SevUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class SevInspector {
    private final SevUsersRepository sevUsersRepository;

    public SevInspector() {
        this.sevUsersRepository = new SevUsersRepository();
    }

    public void grabUserData() throws IOException {
        //            List<String[]> sevUsers = ExcelParser.parseExcel(ExcelDownloader.downloadFile());
        List<String[]> SevUsers = ExcelParser.parseExcel();
        sevUsersRepository.dropData();
        sevUsersRepository.saveSevUsersToDB(SevUsers);
        sevUsersRepository.saveDocFlowUsersToDB();
        sevUsersRepository.markUsersConnectedToSev();
        sevUsersRepository.updateTimestamp();
    }

    public List<SevUser> getUserData() {
        return sevUsersRepository.getAllData();
    }

    public LocalDateTime getLastUpdateTimestamp() {
        return sevUsersRepository.getLastUpdateTimestamp();
    }
}
