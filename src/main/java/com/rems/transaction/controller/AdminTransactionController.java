package com.rems.transaction.controller;

import com.rems.activitylog.service.ActivityLogService;
import com.rems.common.constant.Role;
import com.rems.common.transaction.TransactionManager;
import com.rems.transaction.model.Transaction;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/transactions/*")
public class AdminTransactionController extends HttpServlet {

    private TransactionService transactionService;
    private ActivityLogService activityLogService;

    @Override
    public void init() {
        TransactionManager txManager = new TransactionManager();
        transactionService = new TransactionServiceImpl(txManager, activityLogService);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if (user == null ||
                !(user.getRole() == Role.ADMIN ||
                        user.getRole() == Role.STAFF)) {

            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getPathInfo();

        if (path != null && path.equals("/view")) {

            Long id = Long.valueOf(req.getParameter("id"));

            Transaction tx =
                    transactionService.findById(id);

            req.setAttribute("transaction", tx);

            req.getRequestDispatcher(
                            "/views/admin/transaction-detail.jsp")
                    .forward(req, resp);

            return;
        }

        List<Transaction> transactions =
                transactionService.findAll();

        req.setAttribute("transactions", transactions);

        req.getRequestDispatcher(
                        "/views/admin/transaction-list.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("complete".equals(action)) {

            Long transactionId =
                    Long.parseLong(req.getParameter("transactionId"));

            Long staffId =
                    (Long) req.getSession()
                            .getAttribute("userId");

            try {

                transactionService
                        .completeTransaction(transactionId, staffId);

            } catch (Exception e) {

                req.getSession()
                        .setAttribute("error", e.getMessage());
            }
        }

        resp.sendRedirect(
                req.getContextPath() + "/admin/transactions");
    }
}
