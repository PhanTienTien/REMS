package com.rems.property.controller;

import com.rems.common.util.Factory;
import com.rems.property.dto.PropertyCardDTO;
import com.rems.property.dto.PropertySearchDTO;
import com.rems.property.model.PropertyImage;
import com.rems.property.service.PropertyImageService;
import com.rems.property.service.PropertyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/customer/properties")
public class CustomerPropertyController extends HttpServlet {

    private final PropertyService propertyService = Factory.getPropertyService();
    private final PropertyImageService propertyImageService = Factory.getPropertyImageService();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        PropertySearchDTO searchDTO = new PropertySearchDTO();

        searchDTO.setKeyword(req.getParameter("address"));
        searchDTO.setType(req.getParameter("type"));

        Long minPrice = parseLong(req.getParameter("minPrice"));
        Long maxPrice = parseLong(req.getParameter("maxPrice"));
        searchDTO.setMinPrice(minPrice);
        searchDTO.setMaxPrice(maxPrice);

        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 8);
        searchDTO.setPage(page);
        searchDTO.setSize(size);

        List<PropertyCardDTO> properties =
                propertyService.searchAvailableCard(searchDTO);

        // Get total count for pagination
        int totalItems = propertyService.countCustomer(
                searchDTO.getKeyword(),
                searchDTO.getType(),
                minPrice != null ? minPrice.intValue() : null,
                maxPrice != null ? maxPrice.intValue() : null
        );
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<Long, String> thumbnails = new HashMap<>();

        for (PropertyCardDTO p : properties) {

            List<PropertyImage> images =
                    propertyImageService.getByPropertyId(p.getId());

            if (!images.isEmpty()) {
                thumbnails.put(p.getId(), images.get(0).getImageUrl());
            }
        }

        req.setAttribute("properties", properties);
        req.setAttribute("thumbnails", thumbnails);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);

        req.getRequestDispatcher("/views/customer/property-list.jsp")
                .forward(req, resp);
    }

    private Long parseLong(String val) {
        try {
            return val == null ? null : Long.parseLong(val);
        } catch (Exception e) {
            return null;
        }
    }

    private int parseInt(String val, int def) {
        try {
            return val == null ? def : Integer.parseInt(val);
        } catch (Exception e) {
            return def;
        }
    }
}
