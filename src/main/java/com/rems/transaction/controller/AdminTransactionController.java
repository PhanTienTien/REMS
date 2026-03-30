package com.rems.transaction.controller;

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

@WebServlet("/admin/transactions")
public class AdminTransactionController extends HttpServlet {

    private TransactionService transactionService;

    @Override
    public void init() {
        TransactionManager txManager = new TransactionManager();
        transactionService = new TransactionServiceImpl(txManager, null);
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

        String action = req.getParameter("action");

        // =========================
        // VIEW DETAIL
        // =========================
        if ("view".equals(action)) {

            Long id = Long.valueOf(req.getParameter("id"));

            Transaction tx = transactionService.findById(id);

            req.setAttribute("transaction", tx);

            req.getRequestDispatcher(
                    "/views/admin/transaction-detail.jsp"
            ).forward(req, resp);

            return;
        }

        // =========================
        // LIST + SEARCH + FILTER + PAGING
        // =========================

        String keyword = req.getParameter("keyword");
        String status = req.getParameter("status");
        String sortBy = req.getParameter("sortBy");
        String sortDir = req.getParameter("sortDir");

        int page = parseInt(req.getParameter("page"), 1);
        int size = 10;

        // default sort
        if (sortBy == null) sortBy = "created_at";
        if (sortDir == null) sortDir = "desc";

        List<Transaction> transactions =
                transactionService.searchTransactions(
                        keyword,
                        status,
                        sortBy,
                        sortDir,
                        page,
                        size
                );

        int totalItems =
                transactionService.countTransactions(keyword, status);

        int totalPages =
                (int) Math.ceil((double) totalItems / size);

        // set data
        req.setAttribute("transactions", transactions);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        // giữ filter state
        req.setAttribute("keyword", keyword);
        req.setAttribute("status", status);
        req.setAttribute("sortBy", sortBy);
        req.setAttribute("sortDir", sortDir);

        req.getRequestDispatcher(
                "/views/admin/transaction-list.jsp"
        ).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        if ("complete".equals(action)) {

            Long transactionId =
                    Long.parseLong(req.getParameter("transactionId"));

            try {
                transactionService.completeTransaction(
                        transactionId,
                        user.getId()
                );

            } catch (Exception e) {
                req.getSession().setAttribute("error", e.getMessage());
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/transactions");
    }

    private int parseInt(String val, int defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
