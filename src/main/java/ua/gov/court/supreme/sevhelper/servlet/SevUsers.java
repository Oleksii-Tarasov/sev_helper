package ua.gov.court.supreme.sevhelper.servlet;

import ua.gov.court.supreme.sevhelper.service.ExcelParser;
import ua.gov.court.supreme.sevhelper.service.SevInspector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/downloadUsers")
public class SevUsers extends HttpServlet {
    private final SevInspector sevInspector = new SevInspector();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("GET request received");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST request received");

        try {
//            List<String[]> sevUsers = ExcelParser.parseExcel(ExcelDownloader.downloadFile());
            List<String[]> sevUsers = ExcelParser.parseExcel();
            List<String> docFlowSevUsers = sevInspector.getDocFlowSevUsers();

            req.setAttribute("sevUsers", sevUsers);
            req.setAttribute("docFlowSevUsers", docFlowSevUsers);
        } catch (Exception e) {
            req.setAttribute("error", "Can`t find users " + e.getMessage());
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
