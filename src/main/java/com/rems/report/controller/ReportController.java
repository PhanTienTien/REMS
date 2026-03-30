package com.rems.report.controller;

import com.rems.common.util.Factory;
import com.rems.report.model.dto.ReportDTO;
import com.rems.report.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/admin/reports")
public class ReportController extends HttpServlet {

    private final ReportService reportService = Factory.getReportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("export".equals(action)) {
            exportCSV(resp);
            return;
        }

        List<ReportDTO> reports = reportService.getMonthlyReport();

        req.setAttribute("reports", reports);

        req.getRequestDispatcher("/views/admin/report.jsp")
                .forward(req, resp);
    }

    private void exportCSV(HttpServletResponse resp) throws IOException {

        resp.setContentType("text/csv");
        resp.setHeader("Content-Disposition","attachment; filename=report.csv");

        PrintWriter writer = resp.getWriter();

        writer.println("Month,Revenue,Transactions");

        List<ReportDTO> reports = reportService.getMonthlyReport();

        for (ReportDTO r : reports) {

            writer.println(
                    r.getMonth() + "," +
                            r.getRevenue() + "," +
                            r.getTransactions()
            );
        }

        writer.flush();
    }
}
