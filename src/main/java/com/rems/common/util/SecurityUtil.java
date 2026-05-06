package com.rems.common.util;

import com.rems.common.constant.Role;
import com.rems.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Centralized security utility for authorization checks
 */
public class SecurityUtil {

    /**
     * Check if current user is logged in
     */
    public static boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession(false) != null &&
               req.getSession(false).getAttribute("currentUser") != null;
    }

    /**
     * Get current user from session
     */
    public static User getCurrentUser(HttpServletRequest req) {
        return req.getSession(false) != null
                ? (User) req.getSession(false).getAttribute("currentUser")
                : null;
    }

    /**
     * Check if current user has ADMIN role
     */
    public static boolean isAdmin(HttpServletRequest req) {
        User user = getCurrentUser(req);
        return user != null && user.getRole() == Role.ADMIN;
    }

    /**
     * Check if current user has STAFF role
     */
    public static boolean isStaff(HttpServletRequest req) {
        User user = getCurrentUser(req);
        return user != null && user.getRole() == Role.STAFF;
    }

    /**
     * Check if current user has CUSTOMER role
     */
    public static boolean isCustomer(HttpServletRequest req) {
        User user = getCurrentUser(req);
        return user != null && user.getRole() == Role.CUSTOMER;
    }

    /**
     * Check if current user is ADMIN or STAFF
     */
    public static boolean isAdminOrStaff(HttpServletRequest req) {
        return isAdmin(req) || isStaff(req);
    }

    /**
     * Require user to be logged in, redirect to auth if not
     */
    public static boolean requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return false;
        }
        return true;
    }

    /**
     * Require user to have ADMIN role, send 403 if not
     */
    public static boolean requireAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireLogin(req, resp)) {
            return false;
        }
        
        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return false;
        }
        return true;
    }

    /**
     * Require user to have STAFF role, send 403 if not
     */
    public static boolean requireStaff(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireLogin(req, resp)) {
            return false;
        }
        
        if (!isStaff(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Staff access required");
            return false;
        }
        return true;
    }

    /**
     * Require user to have ADMIN or STAFF role, send 403 if not
     */
    public static boolean requireAdminOrStaff(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireLogin(req, resp)) {
            return false;
        }
        
        if (!isAdminOrStaff(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin or Staff access required");
            return false;
        }
        return true;
    }

    /**
     * Require user to have CUSTOMER role, send 403 if not
     */
    public static boolean requireCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!requireLogin(req, resp)) {
            return false;
        }
        
        if (!isCustomer(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Customer access required");
            return false;
        }
        return true;
    }

    /**
     * Check if user can access property (admin can access all, staff can only access their own)
     */
    public static boolean hasPropertyAccess(HttpServletRequest req, Long propertyCreatedBy, Long staffId) {
        if (isAdmin(req)) {
            return true;
        }
        
        if (isStaff(req)) {
            return staffId != null && staffId.equals(propertyCreatedBy);
        }
        
        return false;
    }
}
