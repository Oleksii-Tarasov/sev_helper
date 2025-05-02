package ua.gov.court.supreme.sevhelper.service;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ua.gov.court.supreme.sevhelper.job.DataGrabJob;

public class SchedulerManager {
    private final Scheduler scheduler;

    public SchedulerManager() throws SchedulerException {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

//    public void startDailyDataGrabScheduler(int hour, int minute, SevInspector sevInspector) throws SchedulerException {
    public void startDailyDataGrabScheduler(SevInspector sevInspector) throws SchedulerException {
        JobDetail updateDataJob = JobBuilder.newJob(DataGrabJob.class)
                .withIdentity("dataGrabJob", "scheduledTasks")
                .build();

        updateDataJob.getJobDataMap().put("sevInspector", sevInspector);

        Trigger dailyTrigger = TriggerBuilder.newTrigger()
                .withIdentity("dataGrabTrigger", "scheduledTasks")
//                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(hour, minute))
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(13, 20))
                .build();

        scheduler.scheduleJob(updateDataJob, dailyTrigger);
        scheduler.start();
    }

    public void stopDailyDataGrabScheduler() throws SchedulerException {
        if (scheduler != null)
            scheduler.shutdown();
    }
}
