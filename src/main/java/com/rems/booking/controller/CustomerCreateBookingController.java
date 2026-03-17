package com.rems.booking.controller;

import com.rems.booking.service.BookingService;
import com.rems.booking.service.impl.BookingServiceImpl;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/bookings/create")
public class CustomerCreateBookingController extends HttpServlet {

    private TransactionManager txManager;

    PropertyService propertyService = new PropertyServiceImpl();
    TransactionService transactionService = new TransactionServiceImpl(txManager);

    private final BookingService bookingService =
            new BookingServiceImpl(propertyService, transactionService);

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {

            Long propertyId =
                    Long.parseLong(request.getParameter("propertyId"));

            String note = request.getParameter("note");

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
                            + "/customer/bookings?id="
                            + bookingId
            );

        } catch (Exception e) {

            throw new ServletException(e);

        }

    }
}
