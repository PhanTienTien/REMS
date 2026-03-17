package com.rems.user.controller;


import com.rems.auth.dao.AuthAccountDAO;
import com.rems.auth.dao.UserOtpDAO;
import com.rems.auth.dao.impl.AuthAccountDAOImpl;
import com.rems.auth.dao.impl.UserOtpDAOImpl;
import com.rems.auth.service.AuthService;
import com.rems.auth.service.impl.AuthServiceImpl;
import com.rems.common.exception.BusinessException;
import com.rems.common.transaction.TransactionManager;
import com.rems.user.dao.UserDAO;
import com.rems.user.dao.impl.UserDAOImpl;
import com.rems.user.model.User;
import com.rems.user.service.UserService;
import com.rems.user.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/customer/profile/*")
public class CustomerProfileController extends HttpServlet {

    private AuthService authService;
    private UserService userService;

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

        userService = new UserServiceImpl(
                txManager,
                userDAO,
                authAccountDAO
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }

        User user = (User) session.getAttribute("currentUser");
        req.setAttribute("user", user);

        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {

            req.getRequestDispatcher(
                    "/views/customer/profile/profile.jsp"
            ).forward(req, resp);

            return;
        }

        if (path.equals("/change-password")) {

            req.getRequestDispatcher(
                    "/views/customer/profile/change-password.jsp"
            ).forward(req, resp);

            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String action = req.getParameter("action");

        if ("changePassword".equals(action)) {
            handleChangePassword(req, resp);
            return;
        }

        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");

        user.setFullName(fullName);
        user.setPhoneNumber(phone);

        userService.updateUser(user);

        session.setAttribute("currentUser", user);

        resp.sendRedirect(req.getContextPath() + "/customer/profile");
    }

    private void handleChangePassword(HttpServletRequest req,
                                      HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("currentUser");

        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/customer/profile?error=confirm");
            return;
        }

        try {
            authService.changePassword(user.getId(), currentPassword, newPassword);

            resp.sendRedirect(req.getContextPath() + "/customer/profile?success=changed");

        } catch (BusinessException e) {
            resp.sendRedirect(req.getContextPath() + "/customer/profile?error=wrongpass");
        }
    }
}
