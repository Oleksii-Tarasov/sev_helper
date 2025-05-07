package ua.gov.court.supreme.sevhelper.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ua.gov.court.supreme.sevhelper.job.DataGrabJob;

public class SchedulerManager {
    private final Scheduler scheduler;
    private final PropertiesLoader propertiesLoader;

    public SchedulerManager() throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
        this.propertiesLoader = PropertiesLoader.getInstance();
    }

    public void startDailyDataGrabScheduler(SevInspector sevInspector) throws SchedulerException {
        JobDetail updateDataJob = JobBuilder.newJob(DataGrabJob.class)
                .withIdentity("dataGrabJob", "scheduledTasks")
                .build();

        updateDataJob.getJobDataMap().put("sevInspector", sevInspector);

        String dbUpdateSchedule = propertiesLoader.getSchedulerCronExpression();

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
