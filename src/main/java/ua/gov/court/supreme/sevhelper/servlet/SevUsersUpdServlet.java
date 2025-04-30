package ua.gov.court.supreme.sevhelper.servlet;

import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/upd-users")
public class SevUsersUpdServlet extends HttpServlet {
    private final SevInspector sevInspector = new SevInspector();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST request received(SevUsersUpdServlet)");
        try {
            sevInspector.grabUserData();
        } catch (Exception e) {
            req.setAttribute("error", "Can`t find users " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath());
    }
}
