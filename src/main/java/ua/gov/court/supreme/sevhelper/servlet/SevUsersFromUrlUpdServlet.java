package ua.gov.court.supreme.sevhelper.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/upd-users")
public class SevUsersFromUrlUpdServlet extends BaseSevServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("POST request received(SevUsersFromUrlUpdServlet)");

        if (!sevInspector.canGrabUserData()) {
            resp.sendRedirect(req.getContextPath());
            return;
        }

        try {
            sevInspector.grabUserDataFromUrl();
        } catch (Exception e) {
            req.setAttribute("error", "Can`t find users " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath());
    }
}
