package com.rems.dashboard.controller;

import com.rems.common.util.Factory;
import com.rems.dashboard.dto.AdminDashboardDTO;
import com.rems.dashboard.dto.StaffDashboardDTO;
import com.rems.dashboard.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class DashboardController extends HttpServlet {

    private final DashboardService dashboardService = Factory.getDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        if ("ADMIN".equals(role)) {

            AdminDashboardDTO dashboard = dashboardService.getAdminDashboard();

            req.setAttribute("dashboard", dashboard);

            req.getRequestDispatcher("/views/admin/dashboard.jsp")
                    .forward(req, resp);

        } else if ("STAFF".equals(role)) {

            Long userId = (Long) session.getAttribute("userId");

            StaffDashboardDTO dashboard = dashboardService.getStaffDashboard(userId);

            req.setAttribute("dashboard", dashboard);

            req.getRequestDispatcher("/views/staff/dashboard.jsp")
                    .forward(req, resp);

        } else {

            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
