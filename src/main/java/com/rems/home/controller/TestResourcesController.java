package com.rems.home.controller;

import com.rems.common.base.BaseController;
import com.rems.common.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test-resources")
public class TestResourcesController extends BaseController {

    private static final boolean DEBUG_ENABLED = isDebugEnabled();

    private static boolean isDebugEnabled() {
        String value = DBUtil.getConfigProperty("app.debug.enabled");
        return "true".equalsIgnoreCase(value);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Test endpoint disabled in production - return 404 to hide existence
        if (!DEBUG_ENABLED) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.getRequestDispatcher("/test-resources.jsp")
                .forward(request, response);
    }
}
