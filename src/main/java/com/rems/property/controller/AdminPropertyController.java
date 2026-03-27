package com.rems.property.controller;

import com.rems.common.constant.PropertyType;
import com.rems.property.dto.CreatePropertyDTO;
import com.rems.property.dto.UpdatePropertyDTO;
import com.rems.property.model.Property;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/properties/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 20
)
public class AdminPropertyController extends HttpServlet {

    private final PropertyService propertyService = new PropertyServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            listProperties(req, resp);
            return;
        }

        switch (path) {

            case "/create":
                showCreateForm(req, resp);
                break;

            case "/edit":
                showEditForm(req, resp);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();

        switch (path) {

            case "/create":
                createProperty(req, resp);
                break;

            case "/edit":
                updateProperty(req, resp);
                break;

            case "/approve":
                approveProperty(req, resp);
                break;

            case "/deactivate":
                deactivateProperty(req, resp);
                break;

            case "/restore":
                restoreProperty(req, resp);
                break;

            case "/delete":
                deleteProperty(req, resp);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listProperties(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = 1;
        int size = 10;

        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        String address = req.getParameter("address");
        String type = req.getParameter("type");

        Integer minPrice = parseInt(req.getParameter("minPrice"));
        Integer maxPrice = parseInt(req.getParameter("maxPrice"));

        List<Property> properties =
                propertyService.searchAdmin(
                        address, type, minPrice, maxPrice, page, size
                );

        int total =
                propertyService.countAdmin(address, type, minPrice, maxPrice);

        int totalPages = (int) Math.ceil((double) total / size);

        req.setAttribute("properties", properties);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/views/admin/property-list.jsp")
                .forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/views/admin/property-create.jsp")
                .forward(req, resp);
    }

    private void createProperty(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String title = req.getParameter("title");
        String address = req.getParameter("address");
        String description = req.getParameter("description");
        String typeStr = req.getParameter("type");
        String priceStr = req.getParameter("price");

        try {

            PropertyType type = PropertyType.valueOf(typeStr);
            BigDecimal price = new BigDecimal(priceStr);

            CreatePropertyDTO dto =
                    new CreatePropertyDTO(title, address, description, type, price);

            dto.validate();

            List<String> imageUrls = new ArrayList<>();

            String uploadPath =
                    getServletContext().getRealPath("/uploads");

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            for (Part part : req.getParts()) {

                if ("images".equals(part.getName()) && part.getSize() > 0) {

                    String fileName =
                            System.currentTimeMillis() + "_"
                                    + Paths.get(part.getSubmittedFileName())
                                    .getFileName()
                                    .toString();

                    String filePath = uploadPath + File.separator + fileName;

                    part.write(filePath);

                    imageUrls.add("/uploads/" + fileName);
                }
            }

            Long staffId = 1L;

            propertyService.createProperty(dto, staffId, imageUrls);

            resp.sendRedirect(req.getContextPath() + "/admin/properties");

        } catch (Exception e) {

            req.setAttribute("error", e.getMessage());

            req.setAttribute("openModal", "create");

            req.setAttribute("formTitle", title);
            req.setAttribute("formAddress", address);
            req.setAttribute("formDescription", description);
            req.setAttribute("formType", typeStr);
            req.setAttribute("formPrice", priceStr);

            req.getRequestDispatcher("/views/admin/property-list.jsp")
                    .forward(req, resp);
        }
    }

    private void updateProperty(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String address = req.getParameter("address");
        String description = req.getParameter("description");
        String typeStr = req.getParameter("type");
        String priceStr = req.getParameter("price");

        try {

            Long id = Long.parseLong(idStr);
            PropertyType type = PropertyType.valueOf(typeStr);
            BigDecimal price = new BigDecimal(priceStr);

            UpdatePropertyDTO dto =
                    new UpdatePropertyDTO(id, title, address, description, type, price);

            dto.validate();

            propertyService.updateProperty(dto);

            resp.sendRedirect(req.getContextPath() + "/admin/properties");

        } catch (Exception e) {

            req.setAttribute("error", e.getMessage());

            req.setAttribute("openModal", "edit");

            req.setAttribute("formId", idStr);
            req.setAttribute("formTitle", title);
            req.setAttribute("formAddress", address);
            req.setAttribute("formDescription", description);
            req.setAttribute("formType", typeStr);
            req.setAttribute("formPrice", priceStr);

            req.getRequestDispatcher("/views/admin/property-list.jsp")
                    .forward(req, resp);
        }
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        Property property = propertyService.getPropertyById(id);

        req.setAttribute("property", property);

        req.getRequestDispatcher("/views/admin/property-edit.jsp")
                .forward(req, resp);
    }



    private void approveProperty(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long propertyId = Long.parseLong(req.getParameter("id"));

        Long staffId = 1L; // sau này lấy từ session

        propertyService.approveProperty(propertyId, staffId);

        resp.sendRedirect(req.getContextPath() + "/admin/properties");
    }

    private void deactivateProperty(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        Long staffId = 1L;

        propertyService.deactivateProperty(id, staffId);

        resp.sendRedirect(req.getContextPath() + "/admin/properties");
    }

    private void restoreProperty(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        Long staffId = 1L;

        propertyService.restoreProperty(id, staffId);

        resp.sendRedirect(req.getContextPath() + "/admin/properties");
    }

    private void deleteProperty(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        propertyService.deleteProperty(id);

        resp.sendRedirect(req.getContextPath() + "/admin/properties");
    }

    private Integer parseInt(String val) {
        try {
            return (val == null || val.isBlank()) ? null : Integer.parseInt(val);
        } catch (Exception e) {
            return null;
        }
    }
}