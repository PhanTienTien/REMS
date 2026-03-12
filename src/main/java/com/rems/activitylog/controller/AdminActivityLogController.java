package com.rems.activitylog.controller;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.dao.impl.ActivityLogDAOImpl;
import com.rems.activitylog.model.ActivityLog;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.common.transaction.TransactionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/activity-logs")
public class AdminActivityLogController extends HttpServlet {

    private ActivityLogService service;

    @Override
    public void init() {

        ActivityLogDAO dao = new ActivityLogDAOImpl();
        TransactionManager txManager = new TransactionManager();

        service = new ActivityLogServiceImpl(dao, txManager);

    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        int page = req.getParameter("page") == null
                ? 1
                : Integer.parseInt(req.getParameter("page"));

        String user = req.getParameter("user");
        String action = req.getParameter("action");
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");

        List<ActivityLog> logs = service.getLogs(page, user, action, fromDate, toDate);

        req.setAttribute("logs", logs);
        req.setAttribute("currentPage", page);

        req.getRequestDispatcher("/views/admin/activity-logs.jsp")
                .forward(req, resp);
    }
}
