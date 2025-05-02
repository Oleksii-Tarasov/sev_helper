package ua.gov.court.supreme.sevhelper.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ua.gov.court.supreme.sevhelper.service.SevInspector;

import java.time.LocalDateTime;

public class DataGrabJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            SevInspector sevInspector = new SevInspector();
            sevInspector.grabUserData();
            System.out.println("SEV db updated: " + LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Помилка при оновленні даних СЕВ: " + e.getMessage());
        }
    }
}
