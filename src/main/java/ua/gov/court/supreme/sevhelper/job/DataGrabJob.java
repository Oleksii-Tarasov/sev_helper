package ua.gov.court.supreme.sevhelper.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import ua.gov.court.supreme.sevhelper.service.SevInspector;
import ua.gov.court.supreme.sevhelper.servlet.SevUsersUpdWebSocket;

import java.time.LocalDateTime;

public class DataGrabJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try {
            JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
            SevInspector sevInspector = (SevInspector) jobDataMap.get("sevInspector");

            sevInspector.grabUserDataFromUrl();
            System.out.println("SEV data updated: " + LocalDateTime.now());

            // Notification to all clients about the update
            SevUsersUpdWebSocket.notifyClients();
        } catch (Exception e) {
            System.err.println("Error updating SEV data" + e.getMessage());
        }
    }
}
