package com.rems.property.controller;

import com.rems.common.constant.PropertyType;
import com.rems.property.dto.CreatePropertyDTO;
import com.rems.property.dto.UpdatePropertyDTO;
import com.rems.common.util.PageResult;
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

        String path = getPath(req);

        switch (path) {
            case "/":
                list(req, resp);
                break;
            case "/create":
                forward(req, resp, "/views/admin/property-create.jsp");
                break;
            case "/edit":
                showEdit(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = getPath(req);

        switch (path) {
            case "/create":
                create(req, resp);
                break;
            case "/edit":
                update(req, resp);
                break;
            case "/approve":
                approve(req, resp);
                break;
            case "/deactivate":
                deactivate(req, resp);
                break;
            case "/restore":
                restore(req, resp);
                break;
            case "/delete":
                delete(req, resp);
                break;
            default:
                resp.sendError(404);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = safePage(req.getParameter("page"));
        int size = safeSize(req.getParameter("size"));

        String keyword = trim(req.getParameter("address"));
        String type = trim(req.getParameter("type"));
        Integer minPrice = parseInt(req.getParameter("minPrice"));
        Integer maxPrice = parseInt(req.getParameter("maxPrice"));
        String sort = trim(req.getParameter("sort"));

        PageResult<Property> result =
                propertyService.searchAdminPage(keyword, type, minPrice, maxPrice, sort, page, size);

        req.setAttribute("result", result);

        int total = propertyService.countAdmin(
                keyword, type, minPrice, maxPrice
        );

        int totalPages = (int) Math.ceil((double) total / size);

        req.setAttribute("properties", result);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("keyword", keyword);
        req.setAttribute("type", type);
        req.setAttribute("minPrice", minPrice);
        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("sort", sort);

        forward(req, resp, "/views/admin/property-list.jsp");
    }

    private void create(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            CreatePropertyDTO dto = buildCreateDTO(req);

            List<String> images = saveImages(req);

            Long staffId = getStaffId(req);

            propertyService.createProperty(dto, staffId, images);

            redirectWithQuery(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("openModal", "create");
            forward(req, resp, "/views/admin/property-list.jsp");
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        try {
            UpdatePropertyDTO dto = buildUpdateDTO(req);

            propertyService.updateProperty(dto);

            redirectWithQuery(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("openModal", "edit");
            forward(req, resp, "/views/admin/property-list.jsp");
        }
    }

    private void showEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        Property property = propertyService.getPropertyById(id);

        req.setAttribute("property", property);

        forward(req, resp, "/views/admin/property-edit.jsp");
    }

    private void approve(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        propertyService.approveProperty(parseLong(req, "id"), getStaffId(req));
        redirectWithQuery(req, resp);
    }

    private void deactivate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        propertyService.deactivateProperty(parseLong(req, "id"), getStaffId(req));
        redirectWithQuery(req, resp);
    }

    private void restore(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        propertyService.restoreProperty(parseLong(req, "id"), getStaffId(req));
        redirectWithQuery(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        propertyService.deleteProperty(parseLong(req, "id"));
        redirectWithQuery(req, resp);
    }

    private String getPath(HttpServletRequest req) {
        String path = req.getPathInfo();
        return (path == null || path.equals("/")) ? "/" : path;
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher(view).forward(req, resp);
    }

    private void redirectWithQuery(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String query = req.getQueryString();

        String url = req.getContextPath() + "/admin/properties";
        if (query != null) {
            url += "?" + query;
        }

        resp.sendRedirect(url);
    }

    private int safePage(String val) {
        int p = parseInt(val, 1);
        return Math.max(p, 1);
    }

    private int safeSize(String val) {
        int s = parseInt(val, 10);
        return Math.min(Math.max(s, 1), 100);
    }

    private Integer parseInt(String val) {
        try {
            return val != null ? Integer.parseInt(val) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private int parseInt(String val, int def) {
        try {
            return val != null ? Integer.parseInt(val) : def;
        } catch (Exception e) {
            return def;
        }
    }

    private Long parseLong(HttpServletRequest req, String name) {
        return Long.parseLong(req.getParameter(name));
    }

    private String trim(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private Long getStaffId(HttpServletRequest req) {
        Object user = req.getSession().getAttribute("userId");
        return user != null ? (Long) user : 1L;
    }

    private CreatePropertyDTO buildCreateDTO(HttpServletRequest req) {
        return new CreatePropertyDTO(
                req.getParameter("title"),
                req.getParameter("address"),
                req.getParameter("description"),
                PropertyType.valueOf(req.getParameter("type")),
                new BigDecimal(req.getParameter("price"))
        );
    }

    private UpdatePropertyDTO buildUpdateDTO(HttpServletRequest req) {
        return new UpdatePropertyDTO(
                Long.parseLong(req.getParameter("id")),
                req.getParameter("title"),
                req.getParameter("address"),
                req.getParameter("description"),
                PropertyType.valueOf(req.getParameter("type")),
                new BigDecimal(req.getParameter("price"))
        );
    }

    private List<String> saveImages(HttpServletRequest req)
            throws IOException, ServletException {

        List<String> list = new ArrayList<>();

        String uploadPath = getServletContext().getRealPath("/uploads");
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        for (Part part : req.getParts()) {
            if ("images".equals(part.getName()) && part.getSize() > 0) {

                String fileName = System.currentTimeMillis() + "_" +
                        Paths.get(part.getSubmittedFileName()).getFileName();

                part.write(uploadPath + File.separator + fileName);

                list.add("/uploads/" + fileName);
            }
        }

        return list;
    }
}