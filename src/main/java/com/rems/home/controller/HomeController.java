package com.rems.home.controller;

import com.rems.activitylog.service.ActivityLogService;
import com.rems.common.util.Factory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private final ActivityLogService activityLogService = Factory.getActivityLogService();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("properties", activityLogService.getMostViewedProperties(6));
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }
}
