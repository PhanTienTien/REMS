package com.rems.booking.controller;

import com.rems.booking.service.BookingService;
import com.rems.common.util.Factory;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/customer/bookings/create")
public class CustomerCreateBookingController extends HttpServlet {

    private final BookingService bookingService = Factory.getBookingService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Long propertyId =
                    Long.parseLong(request.getParameter("propertyId"));

            String note = request.getParameter("note");
            String scheduledAtStr = request.getParameter("scheduledAt");
            LocalDateTime scheduledAt = null;
            if (scheduledAtStr != null && !scheduledAtStr.isBlank()) {
                scheduledAt = LocalDateTime.parse(scheduledAtStr);
            }

            User user =
                    (User) request.getSession()
                            .getAttribute("currentUser");

            Long bookingId =
                    bookingService.createBooking(
                            propertyId,
                            user.getId(),
                            note
                    );

            response.sendRedirect(
                    request.getContextPath()
                            + "/customer/profile/bookings?id="
                            + bookingId
            );

        } catch (Exception e) {

            throw new ServletException(e);

        }

    }
}
