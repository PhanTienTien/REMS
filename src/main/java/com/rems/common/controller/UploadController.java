package com.rems.common.controller;

import com.rems.common.util.FileUploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/uploads/*")
public class UploadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isBlank() || "/".equals(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = pathInfo.substring(1);

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file name");
            return;
        }

        fileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        File file = new File(FileUploadUtil.getUploadDirectory(getServletContext()), fileName);

        if (!file.exists()) {
            file = new File(FileUploadUtil.getLegacyUploadDirectory(), fileName);
        }

        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File canonicalUploadDir = FileUploadUtil.getUploadDirectory(getServletContext()).getCanonicalFile();
        File canonicalLegacyDir = FileUploadUtil.getLegacyUploadDirectory().getCanonicalFile();
        File canonicalFile = file.getCanonicalFile();

        if (!canonicalFile.getPath().startsWith(canonicalUploadDir.getPath()) &&
            !canonicalFile.getPath().startsWith(canonicalLegacyDir.getPath())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        String contentType = getServletContext().getMimeType(file.getName());
        resp.setContentType(contentType != null ? contentType : "application/octet-stream");
        resp.setContentLengthLong(file.length());

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }
}
