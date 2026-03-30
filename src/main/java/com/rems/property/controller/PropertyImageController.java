package com.rems.property.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rems.common.util.Factory;
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
@MultipartConfig
public class PropertyImageController extends HttpServlet {

    private static final String UPLOAD_DIR = "D:/Java/KeySoft/KeyBDS/Image";

    private final PropertyImageService service = Factory.getPropertyImageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

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

        String action = req.getParameter("action");

        if ("add".equals(action)) {

            Long propertyId =
                    Long.valueOf(req.getParameter("propertyId"));

            List<String> urls = null;
            try {
                urls = uploadFiles(req);
            } catch (Exception e) {
                throw new RuntimeException(e);
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

        for (Part part : req.getParts()) {

            if ("images".equals(part.getName()) && part.getSize() > 0) {

                String fileName = part.getSubmittedFileName();

                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) dir.mkdirs();

                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                String filePath = UPLOAD_DIR + File.separator + uniqueFileName;

                part.write(filePath);

                urls.add("/uploads/" + uniqueFileName);
            }
        }

        return urls;
    }
}
