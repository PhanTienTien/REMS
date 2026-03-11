package com.rems.booking.controller;

import com.rems.booking.service.BookingService;
import com.rems.booking.service.impl.BookingServiceImpl;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/bookings")
public class AdminBookingController extends HttpServlet {

    private BookingService bookingService;

    @Override
    public void init() {

        PropertyService propertyService = new PropertyServiceImpl();

        bookingService = new BookingServiceImpl(propertyService);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(
                        "/views/admin/bookings-list.jsp")
                .forward(req, resp);
    }
}