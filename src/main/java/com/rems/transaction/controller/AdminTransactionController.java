package com.rems.transaction.controller;

import com.rems.common.transaction.TransactionManager;
import com.rems.transaction.model.Transaction;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;
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
        transactionService = new TransactionServiceImpl(txManager);
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        List<Transaction> transactions =
                transactionService.findAll();

        req.setAttribute("transactions", transactions);

        req.getRequestDispatcher(
                        "/views/admin/transaction-list.jsp")
                .forward(req, resp);
    }
}
