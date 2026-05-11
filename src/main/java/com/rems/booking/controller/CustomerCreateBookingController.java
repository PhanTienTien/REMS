package com.rems.booking.controller;

import com.rems.booking.service.BookingService;
import com.rems.common.exception.BusinessException;
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
                            note,
                            scheduledAt
                    );

            response.sendRedirect(
                    request.getContextPath()
                            + "/customer/profile/bookings?id="
                            + bookingId
            );

        } catch (BusinessException e) {
            handleError(request, response, e.getMessage());
        } catch (RuntimeException e) {
            // TransactionManager wraps BusinessException in RuntimeException
            if (e.getCause() instanceof BusinessException) {
                handleError(request, response, e.getCause().getMessage());
            } else {
                throw new ServletException(e);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void handleError(HttpServletRequest request,
                             HttpServletResponse response,
                             String errorMessage) throws IOException {
        request.getSession().setAttribute("error", errorMessage);
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/customer/properties");
        }
    }
}
