package com.rems.dashboard.controller;

import com.rems.common.transaction.TransactionManager;
import com.rems.dashboard.dto.AdminDashboardDTO;
import com.rems.dashboard.service.DashboardService;
import com.rems.dashboard.service.impl.DashboardServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/dashboard")
public class DashboardController extends HttpServlet {

    private DashboardService dashboardService;

    @Override
    public void init() {
        TransactionManager transactionManager = new TransactionManager();
        dashboardService = new DashboardServiceImpl(transactionManager);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AdminDashboardDTO dashboard = dashboardService.getAdminDashboard();

        req.setAttribute("dashboard", dashboard);

        req.getRequestDispatcher("/views/admin/dashboard.jsp")
                .forward(req, resp);
    }
}