package ua.gov.court.supreme.sevhelper.servlet;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ua.gov.court.supreme.sevhelper.scheduler.DataGrabJob;
import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server started – initializing...");
        ServletContext servletContext = sce.getServletContext();
        SevInspector sevInspector = new SevInspector();
        servletContext.setAttribute("sevInspector", sevInspector);

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            JobDetail updateDataJob = JobBuilder.newJob(DataGrabJob.class).withIdentity("dataGrabJob", "group1").build();
            Trigger dailyTrigger = TriggerBuilder.newTrigger().withIdentity("dataGrabTrigger")
                    .withIdentity("dailyTrigger", "group1")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(11, 7))
                    .build();

            scheduler.scheduleJob(updateDataJob, dailyTrigger);
            scheduler.start();
        } catch (Exception e) {
            System.err.println("Помилка при ініціалізації планувальника: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            System.err.println("Помилка при зупинці планувальника: " + e.getMessage());
        }
    }
}
