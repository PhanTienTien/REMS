package com.rems.user.controller;

import com.rems.common.exception.BusinessException;
import com.rems.user.model.User;
import com.rems.user.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("register".equals(action)) {
            request.getRequestDispatcher("/views/auth/register.jsp")
                    .forward(request, response);
        } else {
            request.getRequestDispatcher("/views/auth/login.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String action = request.getParameter("action");

        if (action == null) {
            sendJson(response, "error", "Missing action");
            return;
        }

        try {

            switch (action) {

                case "login":
                    handleLogin(request, response);
                    break;

                case "register":
                    handleRegister(request, response);
                    break;

                case "verify":
                    handleVerifyOtp(request, response);
                    break;

                case "resend":
                    handleResendOtp(request, response);
                    break;

                default:
                    sendJson(response, "error", "Invalid action");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendJson(response, "error", "Internal server error");
        }
    }

    private void handleLogin(HttpServletRequest request,
                             HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.login(email, password);

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void handleRegister(HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            authService.register(fullName, email, password);
            sendJson(response, "success", null);

        } catch (BusinessException e) {
            sendJson(response, "error", e.getMessage());
        }
    }

    private void handleVerifyOtp(HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        try {
            authService.verifyOtp(email, otp);
            sendJson(response, "success", null);

        } catch (BusinessException e) {
            sendJson(response, "error", e.getMessage());
        }
    }

    private void handleResendOtp(HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, SQLException {

        String email = request.getParameter("email");

        try {
            authService.resendOtp(email);
            sendJson(response, "success", null);

        } catch (BusinessException e) {
            sendJson(response, "error", e.getMessage());
        }
    }

    private void sendJson(HttpServletResponse response,
                          String status,
                          String message) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        if (message == null) {
            out.write("{\"status\":\"" + status + "\"}");
        } else {
            out.write("{\"status\":\"" + status + "\",\"message\":\""
                    + message.replace("\"", "\\\"") + "\"}");
        }

        out.flush();
    }
}