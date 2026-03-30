package com.rems.activitylog.controller;

import com.rems.activitylog.model.ActivityLog;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.common.constant.Role;
import com.rems.common.util.Factory;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/activity-logs")
public class AdminActivityLogController extends HttpServlet {

    private final ActivityLogService service = Factory.getActivityLogService();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        User currentUser = (User) req.getSession().getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int page = req.getParameter("page") == null
                ? 1
                : Integer.parseInt(req.getParameter("page"));

        String user = req.getParameter("user");
        String action = req.getParameter("action");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");

        List<ActivityLog> logs = service.getLogs(page, user, action, fromDate, toDate);
        int total = service.countLogs(user, action, fromDate, toDate);
        int totalPages = Math.max(1, (int) Math.ceil((double) total / 20));

        req.setAttribute("logs", logs);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("userFilter", user);
        req.setAttribute("actionFilter", action);
        req.setAttribute("fromDateFilter", fromDate);
        req.setAttribute("toDateFilter", toDate);

        req.getRequestDispatcher("/views/admin/activity-logs.jsp")
                .forward(req, resp);
    }
}
