package com.rems.booking.controller;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.booking.service.BookingService;
import com.rems.booking.service.impl.BookingServiceImpl;
import com.rems.common.constant.BookingStatus;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyServiceImpl;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/bookings")
public class AdminBookingController extends HttpServlet {

    private BookingService bookingService;
    private TransactionManager txManager;
    private ActivityLogService activityLogService;
    private ActivityLogDAO activityLogDAO;

    @Override
    public void init() {

        txManager = new TransactionManager();
        activityLogService = new ActivityLogServiceImpl(activityLogDAO, txManager);

        PropertyService propertyService = new PropertyServiceImpl();
        TransactionService transactionService =
                new TransactionServiceImpl(txManager, activityLogService);

        bookingService =
                new BookingServiceImpl(propertyService, transactionService);
    }


    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String statusParam = req.getParameter("status");
        String pageParam = req.getParameter("page");
        String action = req.getParameter("action");

        if ("view".equals(action)) {

            Long bookingId = Long.valueOf(req.getParameter("id"));

            var booking =
                    bookingService.getBookingDetail(bookingId)
                            .orElseThrow(() ->
                                    new RuntimeException("Booking not found"));

            req.setAttribute("booking", booking);

            req.getRequestDispatcher(
                    "/views/admin/booking-detail.jsp"
            ).forward(req, resp);

            return;
        }

        int page = 1;

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        int pageSize = 10;

        var bookings = (statusParam == null)
                ? bookingService.getBookingsPage(page, pageSize)
                : bookingService.getBookingsPageByStatus(
                BookingStatus.valueOf(statusParam),
                page,
                pageSize
        );

        int total = (statusParam == null)
                ? bookingService.countBookings()
                : bookingService.countBookingsByStatus(
                BookingStatus.valueOf(statusParam)
        );

        int totalPages = (int) Math.ceil((double) total / pageSize);

        req.setAttribute("bookings", bookings);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("status", statusParam);

        req.getRequestDispatcher("/views/admin/bookings-list.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        Long bookingId = Long.valueOf(req.getParameter("id"));
        Long staffId = 1L;

        try {

            if ("accept".equals(action)) {

                bookingService.acceptBooking(bookingId, staffId);

            } else if ("reject".equals(action)) {

                bookingService.rejectBooking(bookingId, staffId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(
                req.getContextPath() + "/admin/bookings");
    }
}