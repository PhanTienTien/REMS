package com.rems.transaction.controller;

import com.rems.common.util.Factory;
import com.rems.transaction.model.Transaction;
import com.rems.transaction.service.TransactionService;
import com.rems.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/customer/profile/transactions")
public class CustomerTransactionController extends HttpServlet {

    private final TransactionService transactionService = Factory.getTransactionService();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("currentUser");

        if (user == null) {

            resp.sendRedirect(
                    req.getContextPath() + "/auth"
            );

            return;
        }

        Long customerId = user.getId();

        List<Transaction> transactions =
                transactionService.getByCustomer(customerId);

        req.setAttribute("transactions", transactions);

        req.getRequestDispatcher(
                "/views/customer/profile/transactions.jsp"
        ).forward(req, resp);
    }
}
