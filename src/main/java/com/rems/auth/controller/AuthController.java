package com.rems.auth.controller;

import com.rems.auth.model.AuthAccount;
import com.rems.auth.model.dto.RegisterDto;
import com.rems.auth.service.AuthService;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.util.Factory;
import com.rems.user.model.User;
import com.rems.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    private final AuthService authService = Factory.getAuthService();
    private final UserService userService = Factory.getUserService();

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

    private void handleLogin(HttpServletRequest request,
                             HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {

            AuthAccount account = authService.login(email, password);

            User user = userService.findByAuthId(account.getId());

            HttpSession session = request.getSession();

            session.setAttribute("currentUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole().name());

            if (user.getRole().name().equals("ADMIN")
                    || user.getRole().name().equals("STAFF")) {

                response.sendRedirect(request.getContextPath() + "/admin/dashboard");

            } else {

                response.sendRedirect(request.getContextPath() + "/home");
            }

        } catch (Exception e) {

            Throwable cause = e.getCause();

            if (cause instanceof BusinessException be) {

                ErrorCode code = be.getErrorCode();

                request.setAttribute("error", code.getMessage());

            } else {

                request.setAttribute("error", "Unexpected error occurred");
            }

            request.setAttribute("email", email);

            request.getRequestDispatcher("/views/auth/login.jsp")
                    .forward(request, response);
        }
    }

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

    private void handleVerifyOtp(HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");
        String otp = request.getParameter("otp");

        authService.verifyOtp(email, otp);

        sendJson(response, "success", null);
    }

    private void handleResendOtp(HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        String email = request.getParameter("email");

        authService.resendOtp(email);

        sendJson(response, "success", null);
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
