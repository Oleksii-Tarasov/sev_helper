package ua.gov.court.supreme.sevhelper.servlet;

import org.quartz.SchedulerException;
import ua.gov.court.supreme.sevhelper.service.SchedulerManager;
import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    private SchedulerManager schedulerManager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server started – initializing...");
        ServletContext servletContext = sce.getServletContext();
        SevInspector sevInspector = new SevInspector();
        servletContext.setAttribute("sevInspector", sevInspector);

        try {
            schedulerManager = new SchedulerManager();
            schedulerManager.startDailyDataGrabScheduler(sevInspector);
        } catch (Exception e) {
            System.err.println("Помилка при ініціалізації планувальника: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (schedulerManager != null) {
                schedulerManager.stopDailyDataGrabScheduler();
            }
        } catch (SchedulerException e) {
            System.err.println("Помилка при зупинці планувальника: " + e.getMessage());
        }
    }
}
