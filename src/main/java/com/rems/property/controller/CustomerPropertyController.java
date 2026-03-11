package com.rems.property.controller;

import com.rems.property.model.Property;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/properties")
public class CustomerPropertyController extends HttpServlet {

    private PropertyService propertyService;

    @Override
    public void init() {
        propertyService = new PropertyServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String address = req.getParameter("address");
        String type = req.getParameter("type");
        String sort = req.getParameter("sort");

        Integer minPrice = parseInt(req.getParameter("minPrice"));
        Integer maxPrice = parseInt(req.getParameter("maxPrice"));

        int page = parseInt(req.getParameter("page"), 1);
        int size = 6;

        List<Property> properties =
                propertyService.searchCustomer(
                        address, type, minPrice, maxPrice, sort, page, size
                );

        int total =
                propertyService.countCustomer(
                        address, type, minPrice, maxPrice
                );

        int totalPages =
                (int) Math.ceil((double) total / size);

        req.setAttribute("properties", properties);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher(
                "/views/customer/properties.jsp"
        ).forward(req, resp);
    }

    private Integer parseInt(String val) {
        try { return Integer.parseInt(val); }
        catch (Exception e) { return null; }
    }

    private int parseInt(String val, int def) {
        try { return Integer.parseInt(val); }
        catch (Exception e) { return def; }
    }
}