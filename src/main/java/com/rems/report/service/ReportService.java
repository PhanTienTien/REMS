package com.rems.report.service;

import com.rems.report.model.dto.ReportDTO;
import com.rems.report.model.dto.RevenueReportDTO;
import com.rems.report.model.dto.TransactionStatDTO;

import java.util.List;

public interface ReportService {

    List<TransactionStatDTO> getTransactionStatistics();

    List<RevenueReportDTO> getRevenueReport();

    List<ReportDTO> getMonthlyReport();

}
