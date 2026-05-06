package com.rems.admin.controller;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.util.DBUtil;
import com.rems.common.util.Factory;
import com.rems.common.util.SecurityUtil;
import com.rems.property.model.Property;
import com.rems.property.service.PropertyService;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/debug")
public class AdminDebugController extends HttpServlet {

    private static final boolean DEBUG_ENABLED = isDebugEnabled();

    private static boolean isDebugEnabled() {
        String value = DBUtil.getConfigProperty("app.debug.enabled");
        return "true".equalsIgnoreCase(value);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Debug endpoint disabled in production - return 404 to hide existence
        if (!DEBUG_ENABLED) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Require admin access only - debug should be admin-only
        if (!SecurityUtil.requireAdmin(req, resp)) {
            return;
        }

        User user = SecurityUtil.getCurrentUser(req);

        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Admin Debug Info</h1>");
        resp.getWriter().println("<p>User: " + (user != null ? user.getFullName() : "null") + "</p>");
        resp.getWriter().println("<p>Role: " + (user != null ? user.getRole() : "null") + "</p>");
        resp.getWriter().println("<p>Email: " + (user != null ? user.getEmail() : "null") + "</p>");
        resp.getWriter().println("<p>User ID: " + (user != null ? user.getId() : "null") + "</p>");

        // Test role checking
        boolean isAdminOrStaff = SecurityUtil.isAdminOrStaff(req);
        resp.getWriter().println("<p>Is Admin/Staff: " + isAdminOrStaff + "</p>");

        // Show session info
        resp.getWriter().println("<p>Session ID: " + req.getSession().getId() + "</p>");
        resp.getWriter().println("<p>Session Attribute Names: " +
            java.util.Collections.list(req.getSession().getAttributeNames()).toString() + "</p>");

        // Check DRAFT properties (admin-only)
        if (SecurityUtil.isAdmin(req)) {
            try {
                PropertyService propertyService = Factory.getPropertyService();
                List<Property> draftProperties = propertyService.getPropertiesByStatus(PropertyStatus.DRAFT);
                resp.getWriter().println("<h2>DRAFT Properties (Count: " + draftProperties.size() + ")</h2>");
                resp.getWriter().println("<ul>");
                for (Property prop : draftProperties) {
                    resp.getWriter().println("<li>ID: " + prop.getId() + " - " + prop.getTitle() + " - Status: " + prop.getStatus() + "</li>");
                }
                resp.getWriter().println("</ul>");
            } catch (Exception e) {
                resp.getWriter().println("<p>Error getting DRAFT properties: " + e.getMessage() + "</p>");
            }
        }
    }
}
