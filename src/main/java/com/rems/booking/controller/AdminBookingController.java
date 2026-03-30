package com.rems.booking.controller;

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

    @Override
    public void init() {
        TransactionManager txManager = new TransactionManager();

        ActivityLogService activityLogService =
                new ActivityLogServiceImpl(null, txManager);

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

        String action = req.getParameter("action");

        if ("view".equals(action)) {
            handleViewDetail(req, resp);
            return;
        }

        handleList(req, resp);
    }

    private void handleList(HttpServletRequest req,
                            HttpServletResponse resp)
            throws ServletException, IOException {

        // ===== PARAMS =====
        String keyword = req.getParameter("keyword");
        String statusParam = req.getParameter("status");
        String sort = req.getParameter("sort");

        int page = parseInt(req.getParameter("page"), 1);
        int size = 10;

        // ===== FILTER =====
        BookingStatus status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            status = BookingStatus.valueOf(statusParam);
        }

        // ===== SERVICE =====
        var result = bookingService.searchBookings(
                keyword,
                status,
                sort,
                page,
                size
        );

        // ===== BASE URL (CRITICAL) =====
        String baseUrl = buildBaseUrl(req, keyword, statusParam, sort);

        // ===== SET ATTR =====
        req.setAttribute("result", result);
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", statusParam);
        req.setAttribute("sort", sort);
        req.setAttribute("baseUrl", baseUrl);

        req.getRequestDispatcher("/views/admin/bookings-list.jsp")
                .forward(req, resp);
    }

    private void handleViewDetail(HttpServletRequest req,
                                  HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.valueOf(req.getParameter("id"));

        var booking = bookingService.getBookingDetail(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        req.setAttribute("booking", booking);

        req.getRequestDispatcher("/views/admin/booking-detail.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");
        Long id = Long.valueOf(req.getParameter("id"));
        Long staffId = 1L;

        try {

            switch (action) {
                case "accept" -> bookingService.acceptBooking(id, staffId);
                case "reject" -> bookingService.rejectBooking(id, staffId);
            }

        } catch (Exception e) {
            req.getSession().setAttribute("error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/admin/bookings");
    }

    private int parseInt(String val, int def) {
        try {
            return val == null ? def : Integer.parseInt(val);
        } catch (Exception e) {
            return def;
        }
    }

    private String buildBaseUrl(HttpServletRequest req,
                                String keyword,
                                String status,
                                String sort) {

        StringBuilder url =
                new StringBuilder(req.getContextPath() + "/admin/bookings?");

        if (keyword != null && !keyword.isBlank()) {
            url.append("keyword=").append(keyword).append("&");
        }

        if (status != null && !status.isBlank()) {
            url.append("status=").append(status).append("&");
        }

        if (sort != null && !sort.isBlank()) {
            url.append("sort=").append(sort).append("&");
        }

        return url.toString();
    }
}