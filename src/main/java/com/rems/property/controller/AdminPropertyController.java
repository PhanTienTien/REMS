package com.rems.property.controller;

import com.rems.property.model.Property;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/properties")
public class AdminPropertyController extends HttpServlet {

    private final PropertyService propertyService = new PropertyServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        List<Property> properties = propertyService.getAllProperties();

        req.setAttribute("properties", properties);

        req.getRequestDispatcher("/views/admin/property-list.jsp")
                .forward(req, resp);
    }
}
