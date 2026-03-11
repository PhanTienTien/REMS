package com.rems.user.controller;

import com.rems.auth.dao.impl.AuthAccountDAOImpl;
import com.rems.common.constant.AccountStatus;
import com.rems.common.constant.Role;
import com.rems.common.transaction.TransactionManager;
import com.rems.user.dao.impl.UserDAOImpl;
import com.rems.user.model.User;
import com.rems.user.model.dto.CreateUserByAdminDTO;
import com.rems.user.service.UserService;
import com.rems.user.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/users")
public class AdminUserController extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        TransactionManager txManager = new TransactionManager();
        userService = new UserServiceImpl(txManager, new UserDAOImpl(), new AuthAccountDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) {
            listUsers(req, resp);
            return;
        }

        switch (action) {
            case "search":
                searchUsers(req, resp);
                break;

            case "edit":
                showEditForm(req, resp);
                break;

            case "create":
                req.getRequestDispatcher("/views/admin/user/create.jsp")
                        .forward(req, resp);
                break;

            default:
                listUsers(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        switch (action) {

            case "create":
                createUser(req, resp);
                break;

            case "update":
                updateUser(req, resp);
                break;

            case "delete":
                deleteUser(req, resp);
                break;
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = 1;
        int size = 10;
        String keyword = req.getParameter("keyword");

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        String role = req.getParameter("role");

        if ("ALL".equals(role)) {
            role = null;
        }

        String pageParam = req.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        long totalUsers = userService.countUsers(keyword, role);

        List<User> users = userService.searchUsers(keyword, role, true, page, size);

        int totalPages = (int) Math.ceil((double) totalUsers / size);

        req.setAttribute("users", users);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/views/admin/user-list.jsp")
                .forward(req, resp);
    }

    private void searchUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }
        String role = req.getParameter("role");

        if ("ALL".equals(role)) {
            role = null;
        }

        int page = 1;
        int size = 10;

        String pageParam = req.getParameter("page");

        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        List<User> users = userService.searchUsers(keyword, role, null, page, size);

        long totalUsers = userService.countUsers(keyword, role); // có thể cải tiến sau

        int totalPages = (int) Math.ceil((double) totalUsers / size);

        req.setAttribute("users", users);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        req.getRequestDispatcher("/views/admin/user/property-list.jsp")
                .forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        CreateUserByAdminDTO dto = new CreateUserByAdminDTO(
                req.getParameter("fullName"),
                req.getParameter("email"),
                req.getParameter("phoneNumber"),
                req.getParameter("password"),
                Role.valueOf(req.getParameter("role")),
                AccountStatus.valueOf(req.getParameter("status"))
        );

        userService.createUserByAdmin(dto);

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        User user = new User();
        user.setId(id);
        user.setFullName(req.getParameter("fullName"));
        user.setPhoneNumber(req.getParameter("phoneNumber"));
        user.setRole(Role.valueOf(req.getParameter("role")));
        user.setVerified(Boolean.parseBoolean(req.getParameter("verified")));

        userService.updateUser(user);

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        userService.deleteUser(id);

        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        Optional<User> user = userService.findById(id);

        req.setAttribute("user", user);

        req.getRequestDispatcher("/views/admin/user/edit.jsp")
                .forward(req, resp);
    }
}