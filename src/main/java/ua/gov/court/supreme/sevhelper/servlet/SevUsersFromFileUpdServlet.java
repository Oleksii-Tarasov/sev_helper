package ua.gov.court.supreme.sevhelper.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/upload-file")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,  // 10 MB
        maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class SevUsersFromFileUpdServlet extends BaseSevServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("POST request received(SevUsersFromFileUpdServlet)");

        Part filePart = req.getPart("file");

        // Checking if the file was sent
        if (filePart == null || filePart.getSize() == 0) {
            req.setAttribute("error", "Файл не було завантажено");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        try {
            sevInspector.grabUserDataFromFile(filePart);
        } catch (Exception e) {
            req.setAttribute("error", "Can`t parse file " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath());
    }
}
