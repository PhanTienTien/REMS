package com.rems.booking.controller;

import com.rems.booking.service.BookingService;
import com.rems.common.constant.BookingStatus;
import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.util.Factory;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/admin/bookings")
public class AdminBookingController extends HttpServlet {

    private final BookingService bookingService = Factory.getBookingService();

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

        User user = (User) req.getSession().getAttribute("currentUser");

        var result = user != null && user.getRole() == Role.STAFF
                ? bookingService.searchBookingsByStaff(
                        user.getId(),
                        keyword,
                        status,
                        sort,
                        page,
                        size
                )
                : bookingService.searchBookings(
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
        User user = (User) req.getSession().getAttribute("currentUser");

        var booking = (user != null && user.getRole() == Role.STAFF
                ? bookingService.getBookingDetailForStaff(id, user.getId())
                : bookingService.getBookingDetail(id))
                .orElseThrow(() -> new RuntimeException("Not found"));

        req.setAttribute("booking", booking);

        req.getRequestDispatcher("/views/admin/booking-detail.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if (!isAdmin(user)) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        String action = req.getParameter("action");
        Long id = Long.valueOf(req.getParameter("id"));

        try {

            switch (action) {
                case "accept" -> {
                    if (user.getRole() == Role.STAFF) {
                        bookingService.acceptBookingByStaff(id, user.getId());
                    } else {
                        bookingService.acceptBooking(id, user.getId());
                    }
                    req.getSession().setAttribute("success", "Booking accepted");
                }
                case "reject" -> {
                    if (user.getRole() == Role.STAFF) {
                        bookingService.rejectBookingByStaff(id, user.getId());
                    } else {
                        bookingService.rejectBooking(id, user.getId());
                    }
                    req.getSession().setAttribute("success", "Booking rejected");
                }
            }

        } catch (BusinessException e) {

            req.getSession().setAttribute(
                    "error",
                    e.getErrorCode().getMessage()
            );

        } catch (Exception e) {

            e.printStackTrace(); // hoặc log

            req.getSession().setAttribute(
                    "error",
                    "Internal server error"
            );
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
            url.append("keyword=")
                    .append(URLEncoder.encode(keyword, StandardCharsets.UTF_8))
                    .append("&");
        }

        if (status != null && !status.isBlank()) {
            url.append("status=").append(status).append("&");
        }

        if (sort != null && !sort.isBlank()) {
            url.append("sort=").append(sort).append("&");
        }

        return url.toString();
    }

    private boolean isAdmin(User user) {
        return user != null &&
                (user.getRole() == Role.ADMIN ||
                        user.getRole() == Role.STAFF);
    }
}
