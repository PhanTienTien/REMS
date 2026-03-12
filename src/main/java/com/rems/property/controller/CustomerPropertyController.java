package com.rems.property.controller;

import com.rems.property.dto.PropertyCardDTO;
import com.rems.property.dto.PropertySearchDTO;
import com.rems.property.model.PropertyImage;
import com.rems.property.service.PropertyImageService;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyImageServiceImpl;
import com.rems.property.service.impl.PropertyServiceImpl;
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

    private PropertyService propertyService;
    private PropertyImageService propertyImageService;

    @Override
    public void init() {
        propertyService = new PropertyServiceImpl();
        propertyImageService = new PropertyImageServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        PropertySearchDTO searchDTO = new PropertySearchDTO();

        searchDTO.setKeyword(req.getParameter("keyword"));
        searchDTO.setType(req.getParameter("type"));

        searchDTO.setMinPrice(parseLong(req.getParameter("minPrice")));
        searchDTO.setMaxPrice(parseLong(req.getParameter("maxPrice")));

        searchDTO.setPage(parseInt(req.getParameter("page"), 1));
        searchDTO.setSize(parseInt(req.getParameter("size"), 6));

        List<PropertyCardDTO> properties =
                propertyService.searchAvailableCard(searchDTO);

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