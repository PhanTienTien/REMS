package com.rems.property.controller;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.dao.impl.ActivityLogDAOImpl;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.model.Property;
import com.rems.property.model.PropertyImage;
import com.rems.property.service.PropertyImageService;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyImageServiceImpl;
import com.rems.property.service.impl.PropertyServiceImpl;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/customer/properties/detail")
public class CustomerPropertyDetailController extends HttpServlet {

    private PropertyService propertyService;
    private PropertyImageService imageService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        TransactionManager txmanager = new TransactionManager();
        ActivityLogDAO activityLogDAO = new ActivityLogDAOImpl();

        propertyService = new PropertyServiceImpl();
        imageService = new PropertyImageServiceImpl();
        activityLogService = new ActivityLogServiceImpl(activityLogDAO, txmanager);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long propertyId = parseLong(req.getParameter("id"));

        if (propertyId == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/properties");
            return;
        }

        Property property = propertyService.getPropertyById(propertyId);

        if (property == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/properties");
            return;
        }

        List<PropertyImage> images =
                imageService.getByPropertyId(propertyId);

        String mainImage = null;
        if (!images.isEmpty()) {
            mainImage = images.get(0).getImageUrl();
        }

        List<Property> similar =
                propertyService.findSimilar(property);

        Map<Long, String> thumbnails = new HashMap<>();

        for (Property p : similar) {

            String thumb =
                    imageService.getThumbnail(p.getId());

            thumbnails.put(p.getId(), thumb);
        }

        User user = (User) req.getSession().getAttribute("user");

        if (user != null) {
            activityLogService.logView(user.getId(), propertyId);
        }

        req.setAttribute("property", property);
        req.setAttribute("images", images);
        req.setAttribute("mainImage", mainImage);
        req.setAttribute("similar", similar);
        req.setAttribute("thumbnails", thumbnails);

        req.getRequestDispatcher("/views/customer/property-detail.jsp")
                .forward(req, resp);
    }

    private Long parseLong(String val) {
        try {
            return Long.parseLong(val);
        } catch (Exception e) {
            return null;
        }
    }
}