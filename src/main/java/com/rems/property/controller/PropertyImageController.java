package com.rems.property.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rems.common.util.FileUploadUtil;
import com.rems.common.util.Factory;
import com.rems.common.util.SecurityUtil;
import com.rems.property.service.PropertyImageService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/property-images")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 20
)
public class PropertyImageController extends HttpServlet {

    private final PropertyImageService service = Factory.getPropertyImageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Require admin or staff access
        if (!SecurityUtil.requireAdminOrStaff(req, resp)) {
            return;
        }

        String action = req.getParameter("action");

        if ("list".equals(action)) {

            Long propertyId =
                    Long.valueOf(req.getParameter("propertyId"));

            var images = service.getByPropertyId(propertyId);

            resp.setContentType("application/json");

            new ObjectMapper()
                    .writeValue(resp.getWriter(), images);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Require admin or staff access
        if (!SecurityUtil.requireAdminOrStaff(req, resp)) {
            return;
        }

        String action = req.getParameter("action");

        if ("add".equals(action)) {

            Long propertyId =
                    Long.valueOf(req.getParameter("propertyId"));

            List<String> urls;
            try {
                urls = uploadFiles(req);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                return;
            }

            service.addImagesAction(propertyId, urls);
            resp.sendRedirect(req.getContextPath() + "/admin/properties");

        } else if ("delete".equals(action)) {

            Long id =
                    Long.valueOf(req.getParameter("id"));

            service.deleteImage(id);
        }
    }

    private List<String> uploadFiles(HttpServletRequest req) throws Exception {

        List<String> urls = new ArrayList<>();
        File dir = FileUploadUtil.getLegacyUploadDirectory();

        for (Part part : req.getParts()) {

            if ("images".equals(part.getName()) && part.getSize() > 0) {

                if (!FileUploadUtil.isValidImageFile(part)) {
                    throw new IllegalArgumentException("Invalid file type or size. Only JPG, PNG, GIF, WEBP images under 5MB are allowed.");
                }

                String safeFileName = FileUploadUtil.getSafeFileName(part.getSubmittedFileName());
                if (safeFileName == null) {
                    throw new IllegalArgumentException("Invalid file name.");
                }

                String uniqueFileName = System.currentTimeMillis() + "_" + safeFileName;

                String filePath = new File(dir, uniqueFileName).getAbsolutePath();

                part.write(filePath);

                urls.add("/uploads/" + uniqueFileName);
            }
        }

        return urls;
    }
}
