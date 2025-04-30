package ua.gov.court.supreme.sevhelper.servlet;

import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Server started – initializing...");
        ServletContext servletContext = sce.getServletContext();
        SevInspector sevInspector = new SevInspector();
        servletContext.setAttribute("sevInspector", sevInspector);
    }
}
