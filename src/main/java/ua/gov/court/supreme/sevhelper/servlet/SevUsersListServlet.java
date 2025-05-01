package ua.gov.court.supreme.sevhelper.servlet;

import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("")
public class SevUsersListServlet extends HttpServlet {
    private SevInspector sevInspector;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sevInspector = (SevInspector) config.getServletContext().getAttribute("sevInspector");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request received(SevUsersListServlet)");
        try {
            req.setAttribute("sevUsers", sevInspector.getUserData());

            LocalDateTime lastUpdate = sevInspector.getLastUpdateTimestamp();
            if (lastUpdate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                req.setAttribute("lastUpdate", lastUpdate.format(formatter));
            }

            req.setAttribute("lastUpdateTimestamp", sevInspector.getLastUpdateTimestamp());
        } catch (Exception e) {
            req.setAttribute("error", "Can`t find users " + e.getMessage());
        }

        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
