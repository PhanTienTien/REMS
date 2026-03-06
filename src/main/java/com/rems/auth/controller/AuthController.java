package com.rems.auth.controller;

import com.rems.auth.dao.AuthAccountDAO;
import com.rems.auth.dao.UserOtpDAO;
import com.rems.auth.dao.impl.AuthAccountDAOImpl;
import com.rems.auth.dao.impl.UserOtpDAOImpl;
import com.rems.auth.model.AuthAccount;
import com.rems.auth.service.AuthService;
import com.rems.auth.service.impl.AuthServiceImpl;
import com.rems.common.exception.BusinessException;

import com.rems.auth.model.dto.RegisterDto;
import com.rems.common.transaction.TransactionManager;
import com.rems.user.dao.UserDAO;
import com.rems.user.dao.impl.UserDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        TransactionManager txManager = new TransactionManager();

        AuthAccountDAO authAccountDAO = new AuthAccountDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        UserOtpDAO userOtpDAO = new UserOtpDAOImpl();

        authService = new AuthServiceImpl(
                authAccountDAO,
                userDAO,
                userOtpDAO,
                txManager
        );
    }

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
            sendJson(response, "error", "Invalid action");
            return;
        }

        try {

            switch (action) {

                case "login" -> handleLogin(request, response);
                case "register" -> handleRegister(request, response);
                case "verify" -> handleVerifyOtp(request, response);
                case "resend" -> handleResendOtp(request, response);
                default -> sendJson(response, "error", "Invalid action");
            }

        } catch (BusinessException e) {
            sendJson(response, "error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(response, "error", "Internal server error");
        }
    }

    // ============================
    // LOGIN
    // ============================
    private void handleLogin(HttpServletRequest request,
                             HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        AuthAccount user = authService.login(email, password);

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);

        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }

    // ============================
    // REGISTER
    // ============================
    private void handleRegister(HttpServletRequest request,
                                HttpServletResponse response)
            throws Exception {

        RegisterDto dto = new RegisterDto(
                request.getParameter("userName"),
                request.getParameter("email"),
                request.getParameter("phoneNumber"),
                request.getParameter("password"),
                request.getParameter("confirmPassword")
        );

        authService.register(dto);

        sendJson(response, "success", null);
    }

    // ============================
    // VERIFY OTP
    // ============================
    private void handleVerifyOtp(HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        authService.verifyOtp(email, otp);

        sendJson(response, "success", null);
    }

    // ============================
    // RESEND OTP
    // ============================
    private void handleResendOtp(HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");

        authService.resendOtp(email);

        sendJson(response, "success", null);
    }

    // ============================
    // JSON RESPONSE
    // ============================
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