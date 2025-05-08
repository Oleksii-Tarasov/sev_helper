package ua.gov.court.supreme.sevhelper.servlet;

import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class BaseSevServlet extends HttpServlet {
    protected SevInspector sevInspector;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sevInspector = (SevInspector) config.getServletContext().getAttribute("sevInspector");

        if (sevInspector == null) {
            throw new ServletException("SevInspector is not initialized");
        }
    }
}
