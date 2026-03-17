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

@WebServlet("/customer/profile/bookings")
public class CustomerBookingController extends HttpServlet {

    private BookingService bookingService;
    private TransactionManager txManager;

    @Override
    public void init() {

        PropertyService propertyService = new PropertyServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl(txManager);

        bookingService = new BookingServiceImpl(propertyService, transactionService);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        var currentUser =
                (User) req.getSession().getAttribute("currentUser");

        Long customerId = currentUser.getId();

        var bookings =
                bookingService.getBookingsByCustomer(customerId);

        req.setAttribute("bookings", bookings);

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

            var currentUser =
                    (User) req.getSession().getAttribute("currentUser");

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
}
