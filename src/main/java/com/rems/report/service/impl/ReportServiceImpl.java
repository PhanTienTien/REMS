package com.rems.report.service.impl;

import com.rems.report.dao.ReportDAO;
import com.rems.report.dao.impl.ReportDAOImpl;
import com.rems.report.model.dto.ReportDTO;
import com.rems.report.model.dto.RevenueReportDTO;
import com.rems.report.model.dto.TransactionStatDTO;
import com.rems.report.service.ReportService;

import java.util.List;

public class ReportServiceImpl implements ReportService {

    private final ReportDAO reportDAO = new ReportDAOImpl();

    @Override
    public List<TransactionStatDTO> getTransactionStatistics() {
        return reportDAO.getTransactionStatistics();
    }

    @Override
    public List<RevenueReportDTO> getRevenueReport() {
        return reportDAO.getRevenueByMonth();
    }

    @Override
    public List<ReportDTO> getMonthlyReport() {
        return reportDAO.getMonthlyReport();
    }
}
