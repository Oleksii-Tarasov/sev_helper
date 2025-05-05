package ua.gov.court.supreme.sevhelper.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ua.gov.court.supreme.sevhelper.job.DataGrabJob;

import java.util.Properties;

public class SchedulerManager {
    private final Scheduler scheduler;
    private final Properties properties;

    public SchedulerManager() throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
        this.properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Не вдалося завантажити файл конфігурації", e);
        }
    }

    public void startDailyDataGrabScheduler(SevInspector sevInspector) throws SchedulerException {
        JobDetail updateDataJob = JobBuilder.newJob(DataGrabJob.class)
                .withIdentity("dataGrabJob", "scheduledTasks")
                .build();

        updateDataJob.getJobDataMap().put("sevInspector", sevInspector);

        String dbUpdateSchedule = properties.getProperty("scheduler.cron.expression", "0 15 7,13 * * ?");

        Trigger dailyTrigger = TriggerBuilder.newTrigger()
                .withIdentity("dataGrabTrigger", "scheduledTasks")
                .withSchedule(CronScheduleBuilder.cronSchedule(dbUpdateSchedule))
                .build();

        scheduler.scheduleJob(updateDataJob, dailyTrigger);
        scheduler.start();
    }

    public void stopDailyDataGrabScheduler() throws SchedulerException {
        if (scheduler != null)
            scheduler.shutdown();
    }
}
