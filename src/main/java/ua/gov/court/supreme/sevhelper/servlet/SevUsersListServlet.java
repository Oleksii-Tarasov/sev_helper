package ua.gov.court.supreme.sevhelper.servlet;

import ua.gov.court.supreme.sevhelper.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("")
public class SevUsersListServlet extends BaseSevServlet {
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
        } catch (BusinessException e) {
            req.setAttribute("error", "Помилка отримання даних: " + e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Виникла неочікувана помилка: " + e.getMessage());
        }

        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
