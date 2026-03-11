package com.rems.report.dao;

import com.rems.report.model.dto.ReportDTO;
import com.rems.report.model.dto.RevenueReportDTO;
import com.rems.report.model.dto.TransactionStatDTO;

import java.util.List;

public interface ReportDAO {

    List<TransactionStatDTO> getTransactionStatistics();

    List<RevenueReportDTO> getRevenueByMonth();

    List<ReportDTO> getMonthlyReport();

}
