package com.rems.booking.controller;

import com.rems.booking.dto.CustomerBookingDTO;
import com.rems.booking.service.BookingService;
import com.rems.common.util.Factory;
import com.rems.common.util.PageResult;
import com.rems.common.util.SecurityUtil;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/customer/profile/bookings")
public class CustomerBookingController extends HttpServlet {

    private final BookingService bookingService = Factory.getBookingService();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        // Require customer access
        if (!SecurityUtil.requireCustomer(req, resp)) {
            return;
        }

        var currentUser = SecurityUtil.getCurrentUser(req);

        // Double-check user is not null (shouldn't happen if requireCustomer passed)
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        Long customerId = currentUser.getId();

        // Pagination parameters
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 10);

        // Use database pagination instead of loading all records
        PageResult<CustomerBookingDTO> result = bookingService.getBookingsByCustomerPage(customerId, page, size);

        req.setAttribute("bookings", result.getData());
        req.setAttribute("pagination", result);

        req.getRequestDispatcher(
                "/views/customer/profile/bookings.jsp"
        ).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("cancel".equals(action)) {

            Long bookingId =
                    Long.valueOf(req.getParameter("id"));

            var currentUser = SecurityUtil.getCurrentUser(req);

            // Double-check user is not null
            if (currentUser == null) {
                resp.sendRedirect(req.getContextPath() + "/auth");
                return;
            }

            try {

                bookingService.cancelBooking(
                        bookingId,
                        currentUser.getId()
                );

            } catch (Exception e) {

                req.getSession()
                        .setAttribute("error", e.getMessage());
            }

            resp.sendRedirect(
                    req.getContextPath() + "/customer/profile/bookings"
            );
        }
    }
    
    private int parseInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int result = Integer.parseInt(value.trim());
            return result < 1 ? defaultValue : result;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
