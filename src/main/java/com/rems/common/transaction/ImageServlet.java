package com.rems.common.transaction;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/Java/KeySoft/KeyBDS/Image";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String fileName = pathInfo.substring(1);

        File file = new File(UPLOAD_DIR, fileName);

        if (!file.exists()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType(getServletContext().getMimeType(file.getName()));

        java.nio.file.Files.copy(file.toPath(), resp.getOutputStream());
        resp.getOutputStream().flush();
    }
}
